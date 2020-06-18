package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Jydata;
import com.example.demo.model.Jydetail;
import com.example.demo.model.Jysystm;
import com.example.demo.service.JydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JydataController {
    @Autowired
    JydataService jydataService;
    @ResponseBody
    @RequestMapping("/findjyl/selectAlljy")//查询所有
    public Map<String, Object> selectAlljy() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Jydata> jylist = jydataService.getAll();
            List<Jysystm>  syslist = jydataService.getSystm();
            if (jylist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("jylist", jylist);
                resultMap.put("syslist", syslist);
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
    @RequestMapping("/findjyl/findbydate")//根据时间段和交易名称查询
    public Map<String, Object> findbydate(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String begtime = ob.getJSONObject("bizContent").getString("begtime");
        String endtime = ob.getJSONObject("bizContent").getString("endtime");
        String jysystm = ob.getJSONObject("bizContent").getString("jysystm");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Jydata> datelist = jydataService.getBytime(begtime, endtime, jysystm);
            if (datelist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("datelist", datelist);
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
    @RequestMapping("/findjyl/getDetail")//根据dasn查询单条交易量的详细信息
    public Map<String, Object> getDetail(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String dasn = ob.getJSONObject("bizContent").getString("dasn");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Jydetail> delist = jydataService.getDetail(dasn);
            if (delist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("delist", delist);
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
    @RequestMapping("/findjyl/searchOne")//根据service名称查询单条详情
    public Map<String, Object> searchOne(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        String dasn = ob.getJSONObject("bizContent").getString("dasn");
        String serapi = ob.getJSONObject("bizContent").getString("serapi");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Jydetail> serlist = jydataService.searchOne(serapi, dasn);
            if (serlist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("serlist", serlist);
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
