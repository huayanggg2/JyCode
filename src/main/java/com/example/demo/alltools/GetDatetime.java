package com.example.demo.alltools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDatetime {
    public String dateToday1;
    public String dateToday2;
    public String dateToday;

    public void getDatetime() {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 1);
        dateToday = new SimpleDateFormat("yyyy-MM-dd'T'HH").format(c.getTime());//获取现在时间
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 7);//转换时区

        dateToday1 = new SimpleDateFormat("yyyy-MM-dd'T'HH").format(c.getTime());
        c.add(Calendar.HOUR_OF_DAY, -1);
        dateToday2 = new SimpleDateFormat("yyyy-MM-dd'T'HH").format(c.getTime());
    }

}
