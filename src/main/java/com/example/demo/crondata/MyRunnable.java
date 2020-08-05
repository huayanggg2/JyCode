package com.example.demo.crondata;

import com.example.demo.alltools.GetDatetime;
import com.example.demo.alltools.HttpConn;
import com.example.demo.dao.AnalysisDao;
import com.example.demo.dao.JydataDao;
import com.example.demo.model.Analysis;
import com.example.demo.model.Crondtl;
import com.example.demo.model.jysys.Jydata;
import com.example.demo.model.jysys.Jydetail;
import com.example.demo.service.JydataService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRunnable implements Runnable {

    private Crondtl crondtl;

    private static AnalysisDao analysisDao;
    @Autowired
    public MyRunnable(AnalysisDao analysisDao) {
        MyRunnable.analysisDao = analysisDao;
    }

    public MyRunnable(Crondtl crondtl) {
        this.crondtl = crondtl;
    }

    @Override
    public void run() {
        Analysis analysis = new Analysis();
        GetDatetime gdt = new GetDatetime();
        String dasn = null;
        if(crondtl.getCronkey().equals("互联网TPS")){
            gdt.getMinutetime();
        }else {
            gdt.getDatetime();
        }
        analysis.setJydate(gdt.dateToday);
        analysis.setSysaplatn(crondtl.getSystm());
        analysis.setType(crondtl.getCronkey());

        try {
            //社保医疗
            String requestURL1 = crondtl.getGetapi().replaceFirst("today2",gdt.dateToday2).replaceFirst("today1",gdt.dateToday1);
            JSONArray ja = new HttpConn().getJson(requestURL1);
            System.out.println(ja);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int jycount = (int) jo.getLong("_calc_val");
                analysis.setEntries(jycount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        analysisDao.setAnalysis(analysis);
        /*System.out.println(getapi
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));*/
    }

}