package com.example.demo.controller.influxchart;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author yangxin-ryan
 */
public class DateTimeUtil {

    public static Long parse(String startTime, String timeFormat){
        System.out.println(startTime);
        return Timestamp.valueOf(startTime).getTime();
    }

    public static String format(Date datetime, String retTimeFmt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(retTimeFmt);
        String date = simpleDateFormat.format(datetime);
        return Timestamp.valueOf(date).toString();
}
}