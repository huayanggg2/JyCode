package com.example.demo.controller.qiyuctl;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.agent.Sshhost;
import com.example.demo.model.server.Copnts;
import com.example.demo.model.server.Serverdtl;
import com.example.demo.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Controller
public class ServerController {

    @Autowired
    ServerService serverService;

    @ResponseBody
    @RequestMapping("/server/getcopnts")//获取各个组件状态以及系统性能
    public Map<String, Object> getCopnts() {
        /*List<Nodesj> cptlst = serverService.getCopnts();*/ //另一种方式传递参数，把状态存在cpumem一起传
        List<Copnts> cptlst = serverService.getCopnts();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Serverdtl> sdtlst = serverService.getServerdtl();
        Map<String, List<Copnts>> groupMap1 =
                cptlst.stream().collect(Collectors.groupingBy(Copnts::getNodemc));//根据node名称分组
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        resultMap.put("groupMap1", groupMap1);
        resultMap.put("sdtlst", sdtlst);
        /*   resultMap.put("cptlst", cptlst);*/

        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/server/serOperate")//各个操作组件
    public Map<String, Object> serOperate(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject ob = JSONObject.parseObject(json);
        JSONArray dataList = ob.getJSONObject("bizContent").getJSONArray("dataList");
        String execmd = ob.getJSONObject("bizContent").getString("execmd");
        Sshhost sshhost = new Sshhost();
        Jshell jshell = new Jshell();
        Connection conn = null;
        //String[] result = {""};
        String result = "";
        sshhost.setUsername("root");
        sshhost.setPassword("123456");
        for (int i = 0, ds = dataList.size(); i < ds; i++) {
            JSONObject jbt = JSONObject.parseObject(dataList.get(i).toString());
            sshhost.setHostip(jbt.getString("serverip"));
            String compts = jbt.getString("zujian");
            String cmd = "/home/loginsight/python/bin/supervisorctl " + execmd + " " + compts;
            conn = jshell.login(sshhost);
            result = jshell.execute(conn, cmd);
            System.out.println(result);
        }
        try {
            Thread.sleep(5);
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

}
