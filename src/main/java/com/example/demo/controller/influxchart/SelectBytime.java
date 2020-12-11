package com.example.demo.controller.influxchart;

import com.example.demo.alltools.GetInfluxData;
import com.example.demo.model.mntrdtl.*;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SelectBytime {
@Autowired
    GetInfluxData gid;
    SimpleDateFormat sdf=new SimpleDateFormat("MM/dd HH:mm:ss");
    public Map selectCpuLoad(String sql3, InfluxDB influxDB) {

        List<QueryResult.Result> lsts3 = gid.getInflux(sql3, influxDB);
        Hostrst3 hostrst3 = new Hostrst3();
        List<Hostrst3> obst3 = gid.changeRst(lsts3, hostrst3);
        List timelst = new ArrayList();
        List load1lst = new ArrayList();
        List load5lst = new ArrayList();
        List load15lst = new ArrayList();
        obst3.stream().forEach(item -> {timelst.add(sdf.format(Long.parseLong(String.valueOf(item.getTime()))));load1lst.add(item.getLoad1());load5lst.add(item.getLoad5());load15lst.add(item.getLoad15());});
        Map obst3mp = new HashMap();
        obst3mp.put("timelst",timelst);
        obst3mp.put("load1lst",load1lst);
        obst3mp.put("load5lst",load5lst);
        obst3mp.put("load15lst",load15lst);
        return obst3mp;
    }

    public Map selectMemUse(String sql4, InfluxDB influxDB) {
        List<QueryResult.Result> lsts4 = gid.getInflux(sql4, influxDB);
        Hostrst4 hostrst4 = new Hostrst4();
        List<Hostrst4> obst4 = gid.changeRst(lsts4, hostrst4);
        List timelst = new ArrayList();
        List memusebytm = new ArrayList();
        obst4.stream().forEach(item -> {timelst.add(sdf.format(Long.parseLong(String.valueOf(item.getTime()))));memusebytm.add(item.getUsed_percent());});
        Map obst4mp = new HashMap();
        obst4mp.put("timelst",timelst);
        obst4mp.put("memusebytm",memusebytm);
        return obst4mp;
    }

    public Map selectCpuUse(String sql9, InfluxDB influxDB) {
        List<QueryResult.Result> lsts9 = gid.getInflux(sql9, influxDB);
        Hostrst9 hostrst9 = new Hostrst9();
        List<Hostrst9> obst9 = gid.changeRst(lsts9, hostrst9);
        List timelst = new ArrayList();
        List cpuusebytm = new ArrayList();
        obst9.stream().forEach(item -> {timelst.add(sdf.format(Long.parseLong(String.valueOf(item.getTime()))));cpuusebytm.add(item.getCpu_user());});
        Map obst9mp = new HashMap();
        obst9mp.put("timelst",timelst);
        obst9mp.put("cpuusebytm",cpuusebytm);
        return obst9mp;
    }
    public Map selectDiskIo(String sql8, InfluxDB influxDB) {
        List<QueryResult.Result> lsts8 = gid.getInflux(sql8, influxDB);
        Hostrst8 hostrst8 = new Hostrst8();
        List<Hostrst8> obst8 = gid.changeRst(lsts8, hostrst8);
        List<String> diskset = obst8.stream().map(Hostrst8::getName).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<Long> longtime = obst8.stream().map(Hostrst8::getTime).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<String> disktime = new ArrayList<>();
        longtime.stream().forEach(item -> {disktime.add(sdf.format(Long.parseLong(String.valueOf(item))));});
        List<Netdata> diskdt = new ArrayList<>();
        Map diskmap = new HashMap();
        Map<String, Hostrst8> configMap = obst8.parallelStream().collect(
                Collectors.groupingBy(Hostrst8::getName, // 先根据name分组
                        Collectors.collectingAndThen(
                                Collectors.reducing(( c1,  c2) -> c1.getIo_avg() > c2.getIo_avg() ? c1 : c2), Optional::get)));
        Collection<Hostrst8> valueCollection = configMap.values();
        List<Hostrst8> valueList = new ArrayList<>(valueCollection);
        //Map lastdisk = new HashMap();
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
            //添加最新值
            //lastdisk.put(diskset.get(j),lllst.get(lllst.size()-1));
            valueList.get(j).setLastio((Double) lllst.get(lllst.size()-1));
            diskdt.add(netdata);
        }
        diskmap.put("disktime",disktime);
        diskmap.put("diskdt",diskdt);
        diskmap.put("valueList",valueList);
        return diskmap;
    }

    public Map selectNetdata(String sql6, InfluxDB influxDB){
        List<QueryResult.Result> lsts6 = gid.getInflux(sql6, influxDB);
        Hostrst6 hostrst6 = new Hostrst6();
        List<Hostrst6> obst6 = gid.changeRst(lsts6, hostrst6);
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
        return netmap;
    }
}