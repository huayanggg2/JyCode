package com.example.demo.controller.ezctlagent;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.agent.Sshhost;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;


@Controller
public class DeployController {
    @Value("${liagent.agtpath}")
    private String filepath;

    @ResponseBody//部署agent
    @PostMapping(value = "/jssh/sshLogin",produces = "application/json;charset=UTF-8")
    public Map<String,Object> sshLogin(@RequestBody String json) throws JSONException {
        JSONObject ob = JSONObject.parseObject(json);
        String username = ob.getJSONObject("bizContent").getString("username");
        String hostip = ob.getJSONObject("bizContent").getString("hostip");
        String csdeip = ob.getJSONObject("bizContent").getString("csdeip");
        String password = ob.getJSONObject("bizContent").getString("password");
        String logdir = ob.getJSONObject("bizContent").getString("logdir");
        String[] hostarr = hostip.split(",");//将hostip拆分成数组
        String[] csdearr = csdeip.split(",");//将hostip拆分成数组
        List<String> lst = new ArrayList<String>();
        Sshhost sshhost = new Sshhost();
        Jshell jshell = new Jshell();
        Connection conn = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        sshhost.setUsername(username);
        sshhost.setPassword(password);
        if (hostarr.length > 1) {//当ip个数大于1时
            String[] result;
            String lastres;
            for (int i = 0,hlt = hostarr.length;i < hlt; i++) {
                sshhost.setHostip(hostarr[i]);
                conn = jshell.login(sshhost);
                String cmd = "cd /home && unzip logtool.zip>/dev/null && sh /home/logtool/vxlog.sh " + csdearr[i] + " " + logdir;
                //jshell.scpagt("E:/logtool.zip","/home/",conn);
                jshell.scpagt(filepath,"/home/",conn);
                result = jshell.execute(conn,cmd).split("\n");
                lastres = hostarr[i] + " " + result[result.length - 1];
                lst.add(lastres);
        }
            resultMap.put("status", "0000");
            resultMap.put("message","成功");
            resultMap.put("lst", lst);
            return resultMap;
        } else {//当ip个数为1时
            sshhost.setHostip(hostip);
            conn = jshell.login(sshhost);
            String cmd = "cd /home && unzip logtool.zip>/dev/null && sh /home/logtool/vxlog.sh " + csdeip + " " + logdir;
            //jshell.scpagt("E:/logtool.zip","/home/",conn);
            jshell.scpagt(filepath,"/home/",conn);
            String pattn = ".*失败.*";
            List<JSONObject> jlst = new ArrayList();
            String[] rarr = jshell.execute(conn,cmd).split("\n");
            for (int i = 0,rl = rarr.length; i < rl; i++) {
                JSONObject jsonObject = new JSONObject();
                String rtn = rarr[i];
                boolean isMatch = Pattern.matches(pattn, rtn);
                if(isMatch){
                    jsonObject.put("mesg",rtn);
                    jsonObject.put("stus","error");
                    jlst.add(jsonObject);
                }else{
                    jsonObject.put("mesg",rtn);
                    jsonObject.put("stus","success");
                    jlst.add(jsonObject);
                }
            }
            resultMap.put("status", "0000");
            resultMap.put("message","成功");
            resultMap.put("jlst", jlst);
            return resultMap;
        }
    }


}
