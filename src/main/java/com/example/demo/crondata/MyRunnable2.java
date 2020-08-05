package com.example.demo.crondata;

import com.example.demo.alltools.JqWebSocket;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyRunnable2 implements Runnable{
    @Autowired
    private JqWebSocket jqWebSocket;
    @Override
    public void run() {
        System.out.println("first DynamicTask，"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}