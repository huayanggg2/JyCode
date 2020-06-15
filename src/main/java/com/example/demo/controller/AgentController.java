package com.example.demo.controller;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.Agentpmfc;
import com.example.demo.model.Agentsystm;
import com.example.demo.model.Sshhost;
import com.example.demo.service.AgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AgentController {
    @Autowired
    AgentService agentService;

    @ResponseBody
    @RequestMapping("/agent/selectsystm")//查询所有系统
    public Map<String, Object> selectsystm() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Agentsystm> syslst = agentService.selectsystem();
            if (syslst.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("syslst", syslst);
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
    @RequestMapping("/agent/selectBysystm")//通过系统查询所有ip
    public Map<String, Object> selectBysystm(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String gpsn = ob.getJSONObject("bizContent").getString("gpsn");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Agentpmfc> agtlst = agentService.selectBysystm(gpsn);
            if (agtlst.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("agtlst", agtlst);
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
    @RequestMapping("/agent/selectByIp")//通过ip搜索
    public Map<String, Object> selectByIp(@RequestBody String json) {
        JSONObject obct = JSONObject.parseObject(json);
        String hostip = obct.getJSONObject("bizContent").getString("hostip");
        String[] iparr = hostip.split(",");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        List<Agentpmfc> atc = agentService.selectByIp(iparr);
            if (atc != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("atc", atc);
            } else {
                resultMap.put("status", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", -1);
        }
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/agent/agentOperate")//操作探针
    public Map<String, Object> agentStart(@RequestBody String allips){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(allips);
        String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String execmd = ob.getJSONObject("bizContent").getString("execmd");
        String[] iparr = hostip.split(",");
        Sshhost sshhost = new Sshhost();
      //  List<String> lst = new ArrayList<String>();
        Jshell jshell = new Jshell();
        Connection conn = null;
        sshhost.setUsername("log");
        sshhost.setPassword("log");
        String result = "";
        for (int i = 0; i < iparr.length; i++) {
            sshhost.setHostip(iparr[i]);
            conn = jshell.login(sshhost);
            String cmd = "cd /home/log/loginsight-agent/VxLogSideCar/ && sh vxlog-server.sh "+execmd +" >/dev/null && ps -ef|grep VxLog|grep -v grep|wc -l";
            result = jshell.execute(conn,cmd);
            System.out.println(result);
         //  lst.add(result);
        }
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(result != null){
            resultMap.put("status", "0000");
            resultMap.put("message","成功");
            return resultMap;
        }else {
            resultMap.put("status", "0001");
            resultMap.put("message","失败");
           return resultMap;
    }
    }
    @ResponseBody
    @RequestMapping("/agent/selectCpu")//通过时间段搜索cpu
    public Map<String, Object> selectCpu(@RequestBody String json) {
        JSONObject obct = JSONObject.parseObject(json);
        String hostip = obct.getJSONObject("bizContent").getString("hostip");
        String period = obct.getJSONObject("bizContent").getString("period");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List targetlst = agentService.selectCpu(hostip,period);
            if (targetlst != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("targetlst", targetlst);
            } else {
                resultMap.put("status", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", -1);
        }
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/agent/selectMem")//通过时间段搜索mem
    public Map<String, Object> selectMem(@RequestBody String json) {
        JSONObject obct = JSONObject.parseObject(json);
        String hostip = obct.getJSONObject("bizContent").getString("hostip");
        String period = obct.getJSONObject("bizContent").getString("period");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List targetlst = agentService.selectMem(hostip,period);
            if (targetlst != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("targetlst", targetlst);
            } else {
                resultMap.put("status", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", -1);
        }
        return resultMap;
    }
}
