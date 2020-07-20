package com.example.demo.controller.ezctlserver;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.agent.Agentpmfc;
import com.example.demo.model.agent.Sshhost;
import com.example.demo.model.server.Esmfc;
import com.example.demo.service.MotorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ElasticController {
    @Autowired
    MotorService motorService;

    @ResponseBody
    @RequestMapping("/elstc/selectAlles")
    public Map<String, Object> selectAlles(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        String hostip = ob.getJSONObject("bizContent").getString("hostip");
            String[] iparr = hostip.split(",");
        if (hostip.equals("")){
            iparr = null;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Esmfc> eslst = motorService.selectesByIp(iparr);
            List<Esmfc> alleslst = motorService.selectesByIp(null);
            PageInfo<Esmfc> pageInfo = new PageInfo<Esmfc>(eslst);
            if (eslst.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("alleslst", alleslst);
                resultMap.put("eslst", pageInfo.getList());
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
    @RequestMapping("/elstc/esOperate")//各个操作组件
    public Map<String, Object> esOperate(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        JSONArray dataList = ob.getJSONObject("bizContent").getJSONArray("dataList");
        String execmd = ob.getJSONObject("bizContent").getString("execmd");
        Sshhost sshhost = new Sshhost();
        Jshell jshell = new Jshell();
        Connection conn = null;
        String result = "";
        sshhost.setUsername("root");
        sshhost.setPassword("123456");
        for (int i = 0, ds = dataList.size(); i < ds; i++) {
            //JSONObject jbt = JSONObject.parseObject(dataList.get(i).toString());
            sshhost.setHostip((String) dataList.get(i));
            //sshhost.setHostip(jbt.getString("acsdeip"));
            //String compts = jbt.getString("zujian");
            //String cmd = "/home/loginsight/python/bin/supervisorctl " + execmd + " " + compts;
            String cmd = "/home/loginsight/python/bin/supervisorctl " + execmd + " " + "es-9200";
            conn = jshell.login(sshhost);
            result = jshell.execute(conn, cmd);
            System.out.println(result);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result != null) {
            resultMap.put("status", "0000");
            resultMap.put("message", "成功");

        } else {
            resultMap.put("status", "0001");
            resultMap.put("message", "失败");
        }
        return resultMap;

    }

  /*  @ResponseBody
    @RequestMapping("/elstc/selectesByIp")//通过ip搜索
    public Map<String, Object> selectByIp(@RequestBody String json) {
        JSONObject obct = JSONObject.parseObject(json);
        String hostip = obct.getJSONObject("bizContent").getString("hostip");
        String[] iparr = hostip.split(",");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Esmfc> eslst = motorService.selectesByIp(iparr);
            if (eslst != null) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("eslst", eslst);
            } else {
                resultMap.put("status", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", -1);
        }
        return resultMap;
    }*/


}
