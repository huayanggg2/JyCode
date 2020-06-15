package com.example.demo.controller;

import com.example.demo.model.Jydata;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/jypage")
public class EchartsController {
    @RequestMapping("/alljy")//查询所有传给echarts的值
    public List<Jydata> index(HttpServletRequest request) {
        List<Jydata> getResult = (List<Jydata>) request.getSession().getAttribute("list");
        return getResult;
    }
}
