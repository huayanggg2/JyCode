package com.example.demo.alltools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetDatetime {
    public String dateToday1;
    public String dateToday2;
    public String dateToday;
    Date today = new Date();
    Calendar c = Calendar.getInstance();
    public void getDatetime() {
        c.setTime(today);
        //c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 1);
        dateToday = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());//获取现在时间
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 8);//转换时区

        dateToday1 = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());//定义日期格式
        c.add(Calendar.DAY_OF_MONTH, -1);//获取前一天
        dateToday2 = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }
    public void getMinutetime(){
        c.setTime(today);
        //c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 1);
        dateToday = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(c.getTime());//获取现在时间
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 8);//转换时区

        dateToday1 = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm").format(c.getTime());
        c.add(Calendar.MINUTE, -1);
        dateToday2 = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm").format(c.getTime());
    }

    public String getLastmonth(String timestr) throws ParseException {
        //获取现在时间
        c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(timestr));
        c.add(Calendar.DAY_OF_MONTH, -28);//转换时区，往前推28天，用来计算环比值
        dateToday = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dateToday;
    }

}
