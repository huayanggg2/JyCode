package com.example.demo.crondata;


import com.example.demo.alltools.GetDatetime;
import com.example.demo.alltools.HttpConn;
import com.example.demo.dao.JydataDao;
import com.example.demo.model.Jydata;
import com.example.demo.model.Jydetail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class MyRunnable1 implements Runnable{
    private static JydataDao jydataDao;
    @Autowired
    public MyRunnable1(JydataDao jydataDao) {
        MyRunnable1.jydataDao = jydataDao;
    }

    public MyRunnable1() {

    }

    @Override
    public void run() {
        Jydata jydata = new Jydata();
        GetDatetime gdt = new GetDatetime();
        String dasn = null;
        gdt.getDatetime();
        jydata.setJytime(gdt.dateToday);
        try {
            //社保医疗
            String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=SourceModuleName%3A%E7%A4%BE%E4%BF%9D%E5%8C%BB%E7%96%97%E4%BA%91_uat_mfs_virtual_serviceMonitor.log%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from=" + gdt.dateToday2 + "%3A00%3A00.000Z&to=" + gdt.dateToday1 + "%3A00%3A00.000Z&filter=streams%3A5dccfec54e00b6be25599b55&limit=0&offset=0";
            //客户信息
            //String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=SourceModuleName%3A%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from="+gdt.today2+"%3A00%3A00.000Z&to="+gdt.today1+"%3A00%3A00.000Z&filter=streams%3A5cabf3db4e00b63682da14be&limit=0&offset=0";
            //互联前置
            //String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from="+gdt.today2+"%3A00%3A00.000Z&to="+gdt.today1+"%3A00%3A00.000Z&limit=0&offset=0";
            //String requestURL1 = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22ST%22%20%7C%20calculate%20%22ST%20as%20int%22&from=2020-04-06T16%3A00%3A00.000Z&to=2020-04-07T16%3A00%3A00.000Z&limit=0&offset=0";
            JSONArray ja = new HttpConn().getJson(requestURL1);

            System.out.println(ja);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int jycount = (int) jo.getLong("_calc_val");
                jydata.setJycount(jycount);
                jydata.setJysystm("sbyl");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dasn = "sbyl" + gdt.dateToday;
        jydata.setDasn(dasn);
        jydataDao.setAll(jydata);
        List<Jydetail> ljd = getService(dasn, gdt.dateToday1, gdt.dateToday2);//获取每小时调用的service
        jydataDao.setSer(ljd);
    }
    public List<Jydetail> getService(String dasn, String today1, String today2) {
        List<Jydetail> ljd = new ArrayList<>();
        try {
            //社保医疗
            String hlqzSer = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=SourceModuleName%3A%E7%A4%BE%E4%BF%9D%E5%8C%BB%E7%96%97%E4%BA%91_uat_mfs_virtual_serviceMonitor.log%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from=" + today2 + "%3A00%3A00.000Z&to=" + today1 + "%3A00%3A00.000Z&limit=0&offset=0";
            //客户信息
            //String hlqzSer ="http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=SourceModuleName%3A%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF_serviceMonitor%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from="+today2+"%3A00%3A00.000Z&to="+today1+"%3A00%3A00.000Z&limit=0&offset=0";

            //互联前置
            //String hlqzSer = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from="+today2+"%3A00%3A00.000Z&to="+today1+"%3A00%3A00.000Z&limit=0&offset=0";
            // String hlqzSer = "http://158.222.187.16:12900/api/plugins/com.loginsight.plugins.spl/spl/absolute?query=sourceType%3A%E4%BA%92%E8%81%94%E5%89%8D%E7%BD%AE_server_serviceMonitor%20%20%20%7C%20stats%20sum%28success_tran%29%20as%20%22s%22%20sum%28failure_tran%29%20as%20%22f%22%20by%20service%20%7C%20calculate%20%22s%2F%28s%2Bf%29%2A100%20as%20int%22%20%7C%20sort%20-s%20%7C%20rename%20%22s%22%20as%20%22%E6%88%90%E5%8A%9F%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22f%22%20as%20%22%E5%A4%B1%E8%B4%A5%E4%BA%A4%E6%98%93%22%20%7C%20rename%20%22_calc_val%22%20as%20%22%E6%88%90%E5%8A%9F%E7%8E%87%28%25%29%22&from=2020-04-06T16%3A00%3A00.000Z&to=2020-04-07T16%3A00%3A00.000Z&limit=0&offset=0";
            JSONArray ja = new HttpConn().getJson(hlqzSer);
            System.out.println(ja);


            for (int i = 0; i < ja.length(); i++) {
                Jydetail jydetail = new Jydetail();
                JSONObject jo = ja.getJSONObject(i);
                jydetail.setPerct((int) jo.getLong("成功率(%)"));
                jydetail.setSuces((int) jo.getLong("成功交易"));
                jydetail.setFails((int) jo.getLong("失败交易"));
                jydetail.setSerapi(jo.getString("service"));
                jydetail.setDasn(dasn);
                ljd.add(jydetail);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ljd;
    }
}