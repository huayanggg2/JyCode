package com.example.demo.controller.ezctlserver;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.mntrdtl.Hostrst7;
import com.example.demo.model.server.Copnts;
import com.example.demo.model.server.Monitor;
import com.example.demo.model.server.Serverdtl;
import com.example.demo.service.MotorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MotorController {
    @Autowired
    MotorService motorService;

    @ResponseBody
    @RequestMapping("/motor/getcpuByip")//查询节点cpu
    public Map<String, Object> getcpuByip(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String begintime = ob.getJSONObject("bizContent").getString("begintime");
        String endtime = ob.getJSONObject("bizContent").getString("endtime");
        String ahostip = ob.getJSONObject("bizContent").getString("ahostip");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Serverdtl> getCpu = motorService.getcpuByip(ahostip, begintime, endtime);
            double[][] cpuew = new double[getCpu.size()][];
            String cktm = null;
            String ckcpu = null;
            for (int j = 0, gm = getCpu.size(); j < gm; j++) {
                cktm = getCpu.get(j).getChecktime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date newDate = sdf.parse(cktm);
                ckcpu = getCpu.get(j).getSyscpu();
                cpuew[j] = new double[]{newDate.getTime(), Double.parseDouble(ckcpu)};
            }

            if (getCpu != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("cpuew", cpuew);
            } else {
                resultMap.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", -1);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/motor/getmemByip")//查询节点mem
    public Map<String, Object> getmemByip(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String begintime = ob.getJSONObject("bizContent").getString("begintime");
        String endtime = ob.getJSONObject("bizContent").getString("endtime");
        String ahostip = ob.getJSONObject("bizContent").getString("ahostip");
        //前端需要，所以写的很复杂
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Serverdtl> getMem = motorService.getmemByip(ahostip, begintime, endtime);
            double[][] memew = new double[getMem.size()][];
            String cktm = null;
            String ckmem = null;
            for (int j = 0, gm = getMem.size(); j < gm; j++) {
                cktm = getMem.get(j).getChecktime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date newDate = sdf.parse(cktm);
                ckmem = getMem.get(j).getSysmem();
                memew[j] = new double[]{newDate.getTime(), Double.parseDouble(ckmem)};
            }
            if (getMem != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("memew", memew);
            } else {
                resultMap.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", -1);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/motor/getBytime")//查询所有
    public Map<String, Object> getMonitorBytime(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String begintime = ob.getJSONObject("bizContent").getString("begintime");
        String endtime = ob.getJSONObject("bizContent").getString("endtime");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String thip = null;
        String cktm = null;
        String ckcpu = null;
        String ckmem = null;
        double[][] cpuew;
        double[][] memew;
        try {
            List<Copnts> allnode = motorService.getallnode();
            for (int i = 0, cnde = allnode.size(); i < cnde; i++) {
                Monitor monitor = new Monitor();
                thip = allnode.get(i).getServerip();
                List<Serverdtl> getMem = motorService.getmemByip(thip, begintime, endtime);
                List<Serverdtl> getCpu = motorService.getcpuByip(thip, begintime, endtime);
                monitor.setNodemc(allnode.get(i).getNodemc());
                monitor.setServerip(thip);
                cpuew = new double[getMem.size()][];
                memew = new double[getMem.size()][];
                for (int j = 0, gm = getMem.size(); j < gm; j++) {
                    cktm = getCpu.get(j).getChecktime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date newDate = sdf.parse(cktm);
                    ckcpu = getCpu.get(j).getSyscpu();
                    ckmem = getMem.get(j).getSysmem();
                    cpuew[j] = new double[]{newDate.getTime(), Double.parseDouble(ckcpu)};
                    memew[j] = new double[]{newDate.getTime(), Double.parseDouble(ckmem)};
                }
                monitor.setCktcpu(cpuew);
                monitor.setCktmem(memew);
                resultMap.put("monitor" + i, monitor);

            }
            if (allnode != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");

            } else {
                resultMap.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", -1);
        }
        return resultMap;
    }

}

