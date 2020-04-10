package com.example.demo;

import com.example.demo.model.Jydetail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Component
public class GetService {
    String username = "admin";
    String password = "oneapm";

    public List<Jydetail> getService(String dasn,String today1,String today2){
        List<Jydetail> ljd = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        URLConnection httpConn = null;
        BufferedReader reader = null;
        Crondate cde = new Crondate();
        try {
            //客户信息
            //String hlqzSer ="http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=SourceModuleName%3A%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from="+today2+"T16%3A00%3A00.000Z&to="+today1+"T16%3A00%3A00.000Z&limit=0&offset=0";

            //互联前置
           String hlqzSer = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from="+today2+"T16%3A00%3A00.000Z&to="+today1+"T16%3A00%3A00.000Z&limit=0&offset=0";
           // String hlqzSer = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from=2020-04-06T16%3A00%3A00.000Z&to=2020-04-07T16%3A00%3A00.000Z&limit=0&offset=0";
            String author = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            URL url = new URL(hlqzSer);
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
            System.out.println(ja);


            for (int i = 0; i < ja.length(); i++) {
                Jydetail jydetail = new Jydetail();
                JSONObject jo = ja.getJSONObject(i);
                jydetail.setPerct((int) jo.getLong("成功率(%)"));
                jydetail.setSuces ((int) jo.getLong("成功交易"));
                jydetail.setFails ((int) jo.getLong("失败交易"));
                jydetail.setSerapi(jo.getString("service"));
                jydetail.setDasn(dasn);
                ljd.add(jydetail);
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ljd;
    }

}

