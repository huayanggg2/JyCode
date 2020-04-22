package com.example.demo.controller;

import com.example.demo.model.Jydata;
import com.example.demo.model.Jydetail;
import com.example.demo.service.JydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
public class JydataController {
    @Autowired
    JydataService jydataService;
    String dasn;

    @RequestMapping("/")//查询所有
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("index");
        List<Jydata> list = jydataService.getAll();
        mav.addObject("list",list);
        request.getSession().setAttribute("list",list);//设置excel打印值
        return mav;
    }
    @RequestMapping("/findbydate")//根据时间段和交易名称查询
    public String findbydate(Model model,HttpServletRequest request) {
        String begtime = request.getParameter("begtime");
        String endtime = request.getParameter("endtime");
        String jysystm = request.getParameter("jysystm");
        List<Jydata> list = jydataService.getBytime(begtime,endtime,jysystm);
        request.getSession().setAttribute("list",list);//设置excel打印值
        model.addAttribute("list",list);
        return "index::div1";//页面部分刷新
    }
    @RequestMapping("/getDetail")//查询单条交易详情
    public String getDetail(Model model,HttpServletRequest request) {
        dasn = request.getParameter("dasn");//通过id关联单条交易的detail
        List<Jydetail> delist = jydataService.getDetail(dasn);
        request.getSession().setAttribute("delist",delist);//设置excel打印值
        model.addAttribute("delist",delist);
        return "detailpg";
    }
    @RequestMapping("/searchOne")//根据service名称查询
    public String searchOne(Model model,HttpServletRequest request) {
        String serapi = request.getParameter("serapi");
        List<Jydetail> delist = jydataService.searchOne(serapi,dasn);
        request.getSession().setAttribute("delist",delist);//设置excel打印值
        model.addAttribute("delist",delist);
        return "detailpg::divs";
    }
}
