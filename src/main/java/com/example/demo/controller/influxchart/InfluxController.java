package com.example.demo.controller.influxchart;

import com.example.demo.alltools.GetInfluxData;
import com.example.demo.model.mntrdtl.*;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class InfluxController {
    @Autowired
    private InfluxDB influxDB;
    @Value("${spring.influx.url}")
    private String url;

    GetInfluxData gid = new GetInfluxData();

    @RequestMapping("/influx/get")
    public Map<String, Object> getInflux() {
        String hostip = "158.222.188.168";
        /*String[] sarr = new String[8];
        sarr[0] = "SELECT last(*) FROM cpu where host="+"'"+hostip+"'";
        sarr[1] = ""*/
        String sql = "SELECT last(n_cpus) as n_cpus,last(load5) as cpu_load5 FROM system where host= '" + hostip + "'";
        String sql2 = "SELECT last(total) as mem_total FROM mem where host=" + "'" + hostip + "'";
        String sql3 = "SELECT load1,load5,load15 FROM system WHERE host =\'" + hostip + "\' and time > now() -5m tz('Asia/Shanghai')";
        String sql4 = "SELECT used_percent FROM mem WHERE host = \'" + hostip + "\' and time > now() - 5m tz('Asia/Shanghai')";
        String sql5 = "SELECT MEAN(used_percent) as used_5m FROM mem WHERE host = \'" + hostip + "\' and time > now() - 5m tz('Asia/Shanghai')";
        //String sql6 = "SELECT interface as intfc,bytes_recv,bytes_sent FROM net WHERE host = '158.222.187.112' and interface != 'all' and  time > now() - 5m";
        String sql6 = "SELECT derivative(first(bytes_recv), 1s) as bytes_recv, derivative(first(bytes_sent), 1s) as bytes_sent FROM net WHERE time > now() - 5m AND" +
                " host = \'" + hostip + "\' AND interface != 'all' GROUP BY time(10s), interface tz('Asia/Shanghai')";
        String sql7 = "SELECT last(free) as disk_total,last(used_percent) as disk_uspt FROM disk WHERE host = \'" + hostip + "\' group by path";
       // String sql8 = "SELECT io_time,\"name\" FROM \"diskio\" WHERE time > now() - 5m and host = '158.222.187.112' group by \"name\"";
        String sql8 = "SELECT non_negative_derivative(last(\"weighted_io_time\"),1ms) as io_avg from \"diskio\" WHERE time > now() - 5m and host = \'" + hostip + "\' GROUP BY \"name\",time(10s)";
        String sql9 = "SELECT usage_user as cpu_user FROM cpu WHERE time > now() - 5m and host = \'" + hostip + "\' and cpu = 'cpu-total'";
        String sql10 = "SELECT MEAN(usage_iowait) as avgiw,MEAN(usage_user) as avgus FROM cpu WHERE host = \'" + hostip + "\' and time > now() - 5m tz('Asia/Shanghai')";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //cpu核数,五分钟平均负载
        List<QueryResult.Result> lsts = gid.getInflux(sql, influxDB);
        Hostrst1 point = new Hostrst1();
        List<Hostrst1> obst = gid.changeRst(lsts, point);
        //总内存
        List<QueryResult.Result> lsts2 = gid.getInflux(sql2, influxDB);
        Hostrst2 hostrst2 = new Hostrst2();
        Hostrst2 obst2 = (Hostrst2) gid.changeRst(lsts2, hostrst2).get(0);
        //各时段五分钟cpu负载
        List<QueryResult.Result> lsts3 = gid.getInflux(sql3, influxDB);
        Hostrst3 hostrst3 = new Hostrst3();
        List<Hostrst3> obst3 = gid.changeRst(lsts3, hostrst3);
        //五分钟内存使用率
        List<QueryResult.Result> lsts4 = gid.getInflux(sql4, influxDB);
        Hostrst4 hostrst4 = new Hostrst4();
        List<Hostrst4> obst4 = gid.changeRst(lsts4, hostrst4);
        //五分钟平均内存使用率
        List<QueryResult.Result> lsts5 = gid.getInflux(sql5, influxDB);
        Hostrst5 hostrst5 = new Hostrst5();
        Hostrst5 obst5 = (Hostrst5) gid.changeRst(lsts5, hostrst5).get(0);
        //五分钟网络接收发送流量
        List<QueryResult.Result> lsts6 = gid.getInflux(sql6, influxDB);
        Hostrst6 hostrst6 = new Hostrst6();
        List<Hostrst6> obst6 = gid.changeRst(lsts6, hostrst6);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        List<String> entitySet = obst6.stream().map(Hostrst6::getIntfc).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<Long> lstime = obst6.stream().map(Hostrst6::getTime).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<String> net_time = new ArrayList<>();
        lstime.stream().forEach(item -> {net_time.add(sdf.format(Long.parseLong(String.valueOf(item))));});
        List<Netdata> lntd = new ArrayList<>();
        Map netmap = new HashMap();
        for (int j = 0; j < entitySet.size(); j++) {
            Netdata netdata1 = new Netdata();
            Netdata netdata2 = new Netdata();
            List lllst1 = new ArrayList();
            List lllst2 = new ArrayList();
            for (int i = 0; i < obst6.size(); i++) {
                if (obst6.get(i).getIntfc().equals(entitySet.get(j))) {
                    lllst1.add(obst6.get(i).getBytes_recv());
                    lllst2.add(obst6.get(i).getBytes_sent());
                }
            }
            netdata1.setNetname(entitySet.get(j)+"接收流量");
            netdata1.setDatalist(lllst1);
            netdata2.setNetname(entitySet.get(j)+"发送流量");
            netdata2.setDatalist(lllst2);
            lntd.add(netdata1);
            lntd.add(netdata2);
        }
        netmap.put("net_time",net_time);
        netmap.put("lntd",lntd);
        //五分钟磁盘使用情况
        List<QueryResult.Result> lsts7 = gid.getInflux(sql7, influxDB);
        Hostrst7 hostrst7 = new Hostrst7();
        List<Hostrst7> obst7 = gid.changeRst(lsts7, hostrst7);
        //最大分区使用情况
        Hostrst7 hostmax = obst7.stream().max(Comparator.comparing(Hostrst7::getDisk_total)).get();
        double maxused = hostmax.getDisk_uspt();
        //五分钟磁盘io延时
        List<QueryResult.Result> lsts8 = gid.getInflux(sql8, influxDB);
        Hostrst8 hostrst8 = new Hostrst8();
        List<Hostrst8> obst8 = gid.changeRst(lsts8, hostrst8);
        List<String> diskset = obst8.stream().map(Hostrst8::getName).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<Long> longtime = obst8.stream().map(Hostrst8::getTime).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<String> disktime = new ArrayList<>();
        longtime.stream().forEach(item -> {disktime.add(sdf.format(Long.parseLong(String.valueOf(item))));});
        List<Netdata> diskdt = new ArrayList<>();
        Map diskmap = new HashMap();
        for (int j = 0; j < diskset.size(); j++) {
            Netdata netdata = new Netdata();
            List lllst = new ArrayList();
            for (int i = 0; i < obst8.size(); i++) {
                if (obst8.get(i).getName().equals(diskset.get(j))) {
                    lllst.add(obst8.get(i).getIo_avg());
                }
            }
            netdata.setNetname(diskset.get(j));
            netdata.setDatalist(lllst);
            diskdt.add(netdata);
        }
        Map<String, Hostrst8> configMap = obst8.parallelStream().collect(
                Collectors.groupingBy(Hostrst8::getName, // 先根据appId分组
                        Collectors.collectingAndThen(
                                Collectors.reducing(( c1,  c2) -> c1.getIo_avg() > c2.getIo_avg() ? c1 : c2), Optional::get)));
        Collection<Hostrst8> valueCollection = configMap.values();
        List<Hostrst8> valueList = new ArrayList<>(valueCollection);
        //五分钟cpu使用率
        List<QueryResult.Result> lsts9 = gid.getInflux(sql9, influxDB);
        Hostrst9 hostrst9 = new Hostrst9();
        List<Hostrst9> obst9 = gid.changeRst(lsts9, hostrst9);
        List<QueryResult.Result> lsts10 = gid.getInflux(sql10, influxDB);
        Hostrst10 hostrst10 = new Hostrst10();
        List<Hostrst10> obst10 = gid.changeRst(lsts10, hostrst10);
        diskmap.put("disktime",disktime);
        diskmap.put("diskdt",diskdt);
        List datalst = new ArrayList();
        datalst.add(0,obst.get(0).getCpu_load5());//cpuload5m
        datalst.add(1,obst10.get(0).getAvgiw());//cpuiowait
        datalst.add(2,obst10.get(0).getAvgus());//cpuuse
        datalst.add(3,obst5.getUsed_5m());//mem5m
        datalst.add(4,maxused);//maxdisk
        resultMap.put("n_cpus", obst.get(0).getN_cpus());
        resultMap.put("mem_total", obst2.getMem_total());
        resultMap.put("obst3", obst3);
        resultMap.put("obst4", obst4);
        resultMap.put("datalst", datalst);
        //resultMap.put("obst5", obst5);
        resultMap.put("obst9", obst9);
        resultMap.put("obst6", netmap);
        resultMap.put("obst7", obst7);
        resultMap.put("obst8", diskmap);
        resultMap.put("valueList", valueList);
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }

}