package com.example.demo.crondata;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyRunnable2 implements Runnable{
    @Override
    public void run() {
        System.out.println("first DynamicTask，"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}