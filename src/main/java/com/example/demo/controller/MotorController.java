package com.example.demo.controller;

import com.example.demo.model.Motor;
import com.example.demo.service.MotorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MotorController {
    @Autowired
    MotorService motorService;

    @RequestMapping("/showMotor")//查询所有
    public String motorPage(HttpServletRequest request, Model model) {
        List<Motor> motlist = motorService.getmonitor();
        model.addAttribute("motlist", motlist);
        return "motorPage";
    }
}
