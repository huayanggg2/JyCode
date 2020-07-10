package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.jysys.Jydata;
import com.example.demo.model.jysys.Jydetail;
import com.example.demo.model.jysys.Jysystm;
import com.example.demo.service.JydataService;
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
public class JydataController {
    @Autowired
    JydataService jydataService;
    @ResponseBody
    @RequestMapping("/findjyl/selectAlljy")//查询所有
    public Map<String, Object> selectAlljy(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageHelper.startPage(currentPage,pageSize);
        try {
            List<Jydata> jylist = jydataService.getAll();
            List<Jydata> alljy = jydataService.getAll();
            List<Jysystm>  syslist = jydataService.getSystm();
            Jysystm jstm = new Jysystm();
            jstm.setGpsn("all");
            jstm.setId(syslist.size()+1);
            jstm.setSysgp("所有系统");
            syslist.add(0,jstm);
            PageInfo<Jydata> pageInfo = new PageInfo<Jydata>(jylist);
            if (jylist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("syslist", syslist);
                resultMap.put("alljy",alljy);
                resultMap.put("jylist", pageInfo.getList());
                resultMap.put("pages",pageInfo.getTotal());
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
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        String begtime = ob.getJSONObject("bizContent").getString("begtime");
        String endtime = ob.getJSONObject("bizContent").getString("endtime");
        String jysystm = ob.getJSONObject("bizContent").getString("jysystm");
        PageHelper.startPage(currentPage,pageSize);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Jydata> datelist = jydataService.getBytime(begtime, endtime, jysystm);
            PageInfo<Jydata> pageInfo = new PageInfo<Jydata>(datelist);
            if (datelist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("datelist", pageInfo.getList());
                resultMap.put("pages",pageInfo.getTotal());
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
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        String dasn = ob.getJSONObject("bizContent").getString("dasn");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageHelper.startPage(currentPage,pageSize);
        try {
            List<Jydetail> delist = jydataService.getDetail(dasn);
            List<Jydetail> allde = jydataService.getDetail(dasn);
            PageInfo<Jydetail> pageInfo = new PageInfo<Jydetail>(delist);
            if (delist.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                resultMap.put("delist", pageInfo.getList());
                resultMap.put("allde", allde);
                resultMap.put("pages",pageInfo.getTotal());
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
