package com.example.demo.controller;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.Sshhost;
import com.example.demo.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AutoctlController {
    @Autowired
    AgentService agentService;

    @ResponseBody//修改cpu阀值
    @PostMapping(value = "/site/changeCpu", produces = "application/json;charset=UTF-8")
     public Map<String,Object> changeCpu(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String gpsn = ob.getJSONObject("bizContent").getString("gpsn");
        String cpuValue = ob.getJSONObject("bizContent").getString("cpuValue");
        List<String> all_ips = agentService.selectAllip(gpsn);
        Sshhost sshhost = new Sshhost();
        Jshell jshell = new Jshell();
        Connection conn = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        sshhost.setUsername("log");
        sshhost.setPassword("log");
        String result;
        List<String> strlst = new ArrayList();
        for (int i = 0, leth = all_ips.size(); i < leth; i++) {
            sshhost.setHostip(all_ips.get(i));
            conn = jshell.login(sshhost);
            String cmd = "sed -i 's/cpuvalue.*/cpuvalue=" + cpuValue + "/g' /home/log/check/cpucheck.sh";
            result = jshell.execute(conn, cmd);
            strlst.add(result);
        }
            resultMap.put("status", "0000");
            resultMap.put("message","成功");
            return resultMap;

    }
    @ResponseBody//单台自动控制开关
    @PostMapping(value = "/site/openAuctl", produces = "application/json;charset=UTF-8")
    public Map<String, Object> openAuctl(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONArray iparr = ob.getJSONObject("bizContent").getJSONArray("hostip");
        String action = ob.getJSONObject("bizContent").getString("action");
        Sshhost sshhost = new Sshhost();
        Jshell jshell = new Jshell();
        Connection conn = null;
        sshhost.setUsername("log");
        sshhost.setPassword("log");
        String result;
        List<String> strlst = new ArrayList();
        String cmd = "";
        if(action.equals("open")){
            cmd = "echo -e \"$(crontab -l)\\n */5 * * * * /bin/bash /home/log/check/cpucheck.sh\" | crontab";
        }else {
            cmd = "crontab -l | grep -v \"cpucheck.sh\" | crontab";
        }
        for (int i = 0, leth = iparr.size(); i < leth; i++) {
            sshhost.setHostip((String) iparr.get(i));
            conn = jshell.login(sshhost);
            result = jshell.execute(conn, cmd);
            strlst.add(result);
        }

            resultMap.put("status", "0000");
            resultMap.put("message", "成功");
        return resultMap;
    }
}
