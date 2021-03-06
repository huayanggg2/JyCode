package com.example.demo.controller.ezctlagent;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.agent.Agentpmfc;
import com.example.demo.model.agent.Agentsystm;
import com.example.demo.model.agent.Sshhost;
import com.example.demo.service.AgentService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AgentController {
    @Autowired
    AgentService agentService;

    @ResponseBody
    @RequestMapping("/agent/selectsystm")//查询所有系统
    public Map<String, Object> selectsystm(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Agentsystm> syslst = agentService.selectsystem();
            PageInfo<Agentsystm> pageInfo = new PageInfo<Agentsystm>(syslst);
            if (syslst.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("syslst", pageInfo.getList());
                resultMap.put("pages", pageInfo.getTotal());
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
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        String gpsn = ob.getJSONObject("bizContent").getString("gpsn");
        String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String[] iparr = hostip.split(",");
        if (hostip.equals("")) {
            iparr = null;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Agentpmfc> agtlst = agentService.selectByIp(iparr, gpsn);
            List<Agentpmfc> allagt = agentService.selectByIp(null, gpsn);
            PageInfo<Agentpmfc> pageInfo = new PageInfo<Agentpmfc>(agtlst);
            if (agtlst.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("agtlst", pageInfo.getList());
                resultMap.put("allagt", allagt);
                resultMap.put("pages", pageInfo.getTotal());
            } else {
                resultMap.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", -1);
        }
        return resultMap;
    }

    /* @ResponseBody
     @RequestMapping("/agent/selectByIp")//通过ip搜索
     public Map<String, Object> selectByIp(@RequestBody String json) {
         JSONObject obct = JSONObject.parseObject(json);
         String hostip = obct.getJSONObject("bizContent").getString("hostip");
         String[] iparr = hostip.split(",");
         Map<String, Object> resultMap = new HashMap<String, Object>();
         try {
         List<Agentpmfc> atc = agentService.selectByIp(iparr,"");
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
     }*/
    @ResponseBody
    @RequestMapping("/agent/agentOperate")//操作探针
    public Map<String, Object> agentStart(@RequestBody String allips) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(allips);
        // String hostip = ob.getJSONObject("bizContent").getString("hostip");
        JSONArray iparr = ob.getJSONObject("bizContent").getJSONArray("hostip");
        String execmd = ob.getJSONObject("bizContent").getString("execmd");
        Sshhost sshhost = new Sshhost();
        Jshell jshell = new Jshell();
        Connection conn = null;
        sshhost.setUsername("log");
        sshhost.setPassword("log");
        String result = "";
        for (int i = 0, ipse = iparr.size(); i < ipse; i++) {
            sshhost.setHostip((String) iparr.get(i));
            conn = jshell.login(sshhost);
            String cmd = "cd /home/log/loginsight-agent/VxLogSideCar/ && sh vxlog-server.sh " + execmd + " >/dev/null && ps -ef|grep VxLog|grep -v grep|wc -l";
            result = jshell.execute(conn, cmd);
        }
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result != null) {
            resultMap.put("status", "0000");
            resultMap.put("message", "成功");
            return resultMap;
        } else {
            resultMap.put("status", "0001");
            resultMap.put("message", "失败");
            return resultMap;
        }
    }

    @ResponseBody
    @RequestMapping("/agent/selectCpu")//通过时间段搜索cpu
    public Map<String, Object> selectCpu(@RequestBody String json) {
        JSONObject obct = JSONObject.parseObject(json);
        String hostip = obct.getJSONObject("bizContent").getString("hostip");//服务器ip
        int period = obct.getJSONObject("bizContent").getInteger("period") * (-1);//时间间隔
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            double[][] cpuew;
            String cktm = null;//定义cpu时间
            String ckcpu = null;//定义cpu值
            List relst = new ArrayList();
            List<Agentpmfc> targetlst = agentService.selectCpu(hostip, period);
            cpuew = new double[targetlst.size()][];

            for (int j = 0, ts = targetlst.size(); j < ts; j++) {
                cktm = targetlst.get(j).getChecktime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date newDate = sdf.parse(cktm);
                ckcpu = targetlst.get(j).getCpu();
                cpuew[j] = new double[]{newDate.getTime(), Double.parseDouble(ckcpu)};
                relst.add(cpuew[j]);
            }

            if (targetlst != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("relst", relst);
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
        int period = obct.getJSONObject("bizContent").getInteger("period") * -1;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            double[][] memew;
            String cktm = null;//内存时间
            String ckmem = null;//内存值
            List relst = new ArrayList();
            List<Agentpmfc> targetlst = agentService.selectMem(hostip, period);
            memew = new double[targetlst.size()][];

            for (int j = 0, ts = targetlst.size(); j < ts; j++) {
                cktm = targetlst.get(j).getChecktime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date newDate = sdf.parse(cktm);
                ckmem = targetlst.get(j).getMem();
                memew[j] = new double[]{newDate.getTime(), Double.parseDouble(ckmem)};
                relst.add(memew[j]);
            }

            if (targetlst != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("relst", relst);
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
