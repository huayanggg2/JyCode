package com.example.demo.crondata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.AgtWebsocket;
import com.example.demo.alltools.JqWebSocket;
import com.example.demo.model.server.Esmfc;
import com.example.demo.service.MotorService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EsStatus {

    @Autowired
    private JqWebSocket jqWebSocket;
    @Autowired
    private AgtWebsocket agtWebsocket;
    @Autowired
    MotorService motorService;

        @Scheduled(cron = "*/5 * * * * ?")
        @Async
        public void realEs() throws Exception{
            List<Esmfc> lses =  motorService.selectAlles();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lses", lses);
            JSONObject ob = JSONObject.parseObject(JSON.toJSONString(resultMap));
            String str = ob.toString();
            jqWebSocket.sendMessage();
        }

    @Scheduled(cron = "*/5 * * * * ?")
    @Async
    public void realAgent() throws Exception{
        List<Esmfc> lses =  motorService.selectAlles();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("lses", lses);
        JSONObject ob = JSONObject.parseObject(JSON.toJSONString(resultMap));
        String str = ob.toString();
        agtWebsocket.sendMessage(str);
    }

}