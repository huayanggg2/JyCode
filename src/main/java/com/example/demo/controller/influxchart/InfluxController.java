package com.example.demo.controller.influxchart;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.GetInfluxData;
import com.example.demo.model.mntrdtl.*;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class InfluxController {
    @Autowired
    private InfluxDB influxDB;
    @Autowired
    private SelectBytime scl;

    GetInfluxData gid = new GetInfluxData();

    @RequestMapping("/influx/get")
    public Map<String, Object> getInflux() {
        String hostip = "158.222.188.168";
        String sql = "SELECT last(n_cpus) as n_cpus,last(load5) as cpu_load5 FROM system where host= '" + hostip + "'";
        String sql2 = "SELECT last(total) as mem_total FROM mem where host=" + "'" + hostip + "'";
        String sql3 = "SELECT load1,load5,load15 FROM system WHERE host ='" + hostip + "' and time > now() -5m tz('Asia/Shanghai')";
        String sql4 = "SELECT used_percent FROM mem WHERE host = '" + hostip + "' and time > now() - 5m tz('Asia/Shanghai')";
        String sql5 = "SELECT MEAN(used_percent) as used_5m FROM mem WHERE host = '" + hostip + "' and time > now() - 5m tz('Asia/Shanghai')";
        String sql6 = "SELECT derivative(first(bytes_recv), 1s) as bytes_recv, derivative(first(bytes_sent), 1s) as bytes_sent FROM net WHERE time > now() - 5m AND" +
                " host = '" + hostip + "' AND interface != 'all' GROUP BY time(1s), interface tz('Asia/Shanghai')";
        String sql7 = "SELECT last(free) as disk_total,last(used_percent) as disk_uspt FROM disk WHERE host = '" + hostip + "' group by path";
        String sql8 = "SELECT non_negative_derivative(last(\"weighted_io_time\"),1ms) as io_avg from \"diskio\" WHERE time > now() - 5m and host = '" + hostip + "' GROUP BY \"name\",time(1s)";
        String sql9 = "SELECT usage_user as cpu_user FROM cpu WHERE time > now() - 5m and host = '" + hostip + "' and cpu = 'cpu-total'";
        String sql10 = "SELECT MEAN(usage_iowait) as avgiw,MEAN(usage_user) as avgus FROM cpu WHERE host = '" + hostip + "' and time > now() - 5m tz('Asia/Shanghai')";
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
        Map obst3mp = scl.selectCpuLoad(sql3,influxDB);
        //五分钟内存使用率
        Map obst4mp = scl.selectMemUse(sql4,influxDB);
        //五分钟平均内存使用率
        List<QueryResult.Result> lsts5 = gid.getInflux(sql5, influxDB);
        Hostrst5 hostrst5 = new Hostrst5();
        Hostrst5 obst5 = (Hostrst5) gid.changeRst(lsts5, hostrst5).get(0);
        //五分钟网络接收发送流量
         Map netmap = scl.selectNetdata(sql6,influxDB);
        //五分钟磁盘使用情况
        List<QueryResult.Result> lsts7 = gid.getInflux(sql7, influxDB);
        Hostrst7 hostrst7 = new Hostrst7();
        List<Hostrst7> obst7 = gid.changeRst(lsts7, hostrst7);
        //最大分区使用情况
        Hostrst7 hostmax = obst7.stream().max(Comparator.comparing(Hostrst7::getDisk_total)).get();
        double maxused = hostmax.getDisk_uspt();
        //五分钟磁盘io延时
        Map diskmap = scl.selectDiskIo(sql8,influxDB);
        //五分钟内磁盘io最大值

        //五分钟cpu使用率
        Map obst9mp = scl.selectCpuUse(sql9,influxDB);
        List<QueryResult.Result> lsts10 = gid.getInflux(sql10, influxDB);
        Hostrst10 hostrst10 = new Hostrst10();
        List<Hostrst10> obst10 = gid.changeRst(lsts10, hostrst10);
        List datalst = new ArrayList();
        datalst.add(0,obst.get(0).getCpu_load5());//cpuload5m
        datalst.add(1,obst10.get(0).getAvgiw());//cpuiowait
        datalst.add(2,obst10.get(0).getAvgus());//cpuuse
        datalst.add(3,obst5.getUsed_5m());//mem5m
        datalst.add(4,maxused);//maxdisk
        resultMap.put("n_cpus", obst.get(0).getN_cpus());
        resultMap.put("mem_total", obst2.getMem_total());
        resultMap.put("obst3mp", obst3mp);
        resultMap.put("obst4mp", obst4mp);
        resultMap.put("datalst", datalst);
        resultMap.put("obst9mp", obst9mp);
        resultMap.put("obst6", netmap);
        resultMap.put("obst7", obst7);
        resultMap.put("obst8", diskmap);
        resultMap.put("valueList", diskmap.get("valueList"));
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/influx/getCpuLoad")
    public Map<String, Object> getCpuLoad(@RequestBody String json) {
        String hostip = "158.222.188.168";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        //String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String begintm = ob.getJSONObject("bizContent").getString("begintm");
        String endtm =  ob.getJSONObject("bizContent").getString("endtm");
        String sql3 = "SELECT load1,load5,load15 FROM system WHERE host ='" + hostip + "' and time > '"+begintm+"' and time < '"+endtm+"'  tz('Asia/Shanghai')";
        Map cpuLoadbytm = scl.selectCpuLoad(sql3,influxDB);
        resultMap.put("cpuLoadbytm", cpuLoadbytm);
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/influx/getMemUse")
    public Map<String, Object> getMemUse(@RequestBody String json) {
        String hostip = "158.222.188.168";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        //String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String begintm = ob.getJSONObject("bizContent").getString("begintm");
        String endtm =  ob.getJSONObject("bizContent").getString("endtm");
        String sql4 = "SELECT used_percent FROM mem WHERE host ='" + hostip + "' and time > '"+begintm+"' and time < '"+endtm+"'  tz('Asia/Shanghai')";
        Map memUsebytm = scl.selectMemUse(sql4,influxDB);
        resultMap.put("memUsebytm", memUsebytm);
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/influx/getCpuUse")
    public Map<String, Object> getCpuUse(@RequestBody String json) {
        String hostip = "158.222.188.168";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        //String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String begintm = ob.getJSONObject("bizContent").getString("begintm");
        String endtm =  ob.getJSONObject("bizContent").getString("endtm");
        String sql9 = "SELECT usage_user as cpu_user FROM cpu WHERE host ='" + hostip + "' and cpu = 'cpu-total' and time > '"+begintm+"' and time < '"+endtm+"'  tz('Asia/Shanghai')";
        Map cpuUsebytm = scl.selectCpuUse(sql9,influxDB);
        resultMap.put("cpuUsebytm", cpuUsebytm);
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/influx/getDiskIo")
    public Map<String, Object> getDiskIo(@RequestBody String json) {
        String hostip = "158.222.188.168";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        //String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String begintm = ob.getJSONObject("bizContent").getString("begintm");
        String endtm =  ob.getJSONObject("bizContent").getString("endtm");
        String sql8 = "SELECT non_negative_derivative(last(\"weighted_io_time\"),1ms) as io_avg FROM diskio WHERE host ='" + hostip + "' and time > '"+begintm+"' and time < '"+endtm+"' GROUP BY \"name\",time(1s)  tz('Asia/Shanghai')";
        Map diskIobytm = scl.selectDiskIo(sql8,influxDB);
        resultMap.put("diskIobytm", diskIobytm);
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/influx/getNetdata")
    public Map<String, Object> getNetdata(@RequestBody String json) {
        String hostip = "158.222.188.168";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        //String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String begintm = ob.getJSONObject("bizContent").getString("begintm");
        String endtm =  ob.getJSONObject("bizContent").getString("endtm");
        String sql6 = "SELECT derivative(first(bytes_recv), 1s) as bytes_recv, derivative(first(bytes_sent), 1s) as bytes_sent FROM net WHERE host ='" + hostip + "' and time > '"+begintm+"' and time < '"+endtm+"' AND interface != 'all' GROUP BY time(1s), interface tz('Asia/Shanghai')";
        Map netDatabytm = scl.selectNetdata(sql6,influxDB);
        resultMap.put("netDatabytm", netDatabytm);
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }
}