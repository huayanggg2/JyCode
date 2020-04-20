package com.example.demo.alltools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDatetime {
        public String today1;
        public String today2;
        public String dateToday;
        public void getDatetime() {
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            dateToday = new SimpleDateFormat("yyyy-MM-dd HH").format(c.getTime());
            c.set(Calendar.HOUR, c.get(Calendar.HOUR) -8);
            /*c.add(Calendar.HOUR_OF_DAY,-8);*/
            String dateToday1 = new SimpleDateFormat("yyyy-MM-dd HH").format(c.getTime());
            today1 = dateToday1.replace(" ","T");
            c.add(Calendar.HOUR_OF_DAY,-1);
            String dateToday2 = new SimpleDateFormat("yyyy-MM-dd HH").format(c.getTime());
            today2 = dateToday2.replace(" ","T");
    }

}
