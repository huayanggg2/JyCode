package com.example.demo;

import com.example.demo.dao.JydataDao;
import com.example.demo.model.Jydata;
import com.example.demo.model.Jydetail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class Crondate {
    @Autowired
    JydataDao jydataDao;
    GetService gse =  new GetService();
    String dasn = null;
    @Scheduled(cron = "0 0 2 * * *")
    public void scheduled1(){
        Jydata jydata = new Jydata();
        StringBuffer buffer = new StringBuffer();
        URLConnection httpConn = null;
        BufferedReader reader = null;
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        String today1= new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        String yestoday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, -1);
        String today2= new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        jydata.setJytime(yestoday);
        try {
            //客户信息
            //String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=SourceModuleName%3A%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from="+today2+"T16%3A00%3A00.000Z&to="+today1+"T16%3A00%3A00.000Z&filter=streams%3A5cabf3db4e00b63682da14be&limit=0&offset=0";
            //互联前置
            String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from="+today2+"T16%3A00%3A00.000Z&to="+today1+"T16%3A00%3A00.000Z&limit=0&offset=0";
            //String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from=2020-04-06T16%3A00%3A00.000Z&to=2020-04-07T16%3A00%3A00.000Z&limit=0&offset=0";
            String username = "admin";
            String password = "oneapm";
            String author = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            URL url = new URL(requestURL1);
            httpConn = (HttpURLConnection) url.openConnection();
            ((HttpURLConnection) httpConn).setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "text/plain");
            httpConn.setRequestProperty("charset", "UTF-8");
            httpConn.setRequestProperty("Authorization", author);
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject ob = new JSONObject(buffer.toString());
            JSONArray ja = ob.getJSONArray("result");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int jycount = (int) jo.getLong("_calc_val");
                jydata.setJycount(jycount);
                jydata.setJysystm("互联前置");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dasn = "hlqz"+today1;
        jydata.setDasn(dasn);
        jydataDao.setAll(jydata);

      List<Jydetail> ljd = gse.getService(dasn,today1,today2);
      jydataDao.setSer(ljd);
    }

}
