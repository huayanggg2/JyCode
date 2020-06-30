package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Agentsystm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LgistController {
    @ResponseBody
    @RequestMapping("/serdtl/selectallser")//查询server端各节点
    public Map<String, Object> selectallser(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        /*JSONObject ob = JSONObject.parseObject(json);
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        PageHelper.startPage(currentPage,pageSize);*/

        return resultMap;
    }
}
