package com.example.demo.controller.ezctlcrontask;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.alltools.GetDatetime;
import com.example.demo.model.Analysis;
import com.example.demo.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.*;

@Controller
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @ResponseBody
    @RequestMapping("/anlys/slctonesys")
    public Map<String, Object> slctonesys(@RequestBody String json) throws ParseException {
        GetDatetime gdt = new GetDatetime();
        JSONObject ob = JSONObject.parseObject(json);
        String fxbgtm = ob.getJSONObject("bizContent").getString("fxbgtm");
        String fxedtm = ob.getJSONObject("bizContent").getString("fxedtm");
        String sysaplatn = ob.getJSONObject("bizContent").getString("sysaplatn");
        String lstfxbgtm = gdt.getLastmonth(fxbgtm);
        String lstfxedtm = gdt.getLastmonth(fxedtm);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] iparr = sysaplatn.split(",");
        List<String> timels = new ArrayList<>();//定义时间list
        List<Integer> jlyls = new ArrayList<>();//定义交易量list
        double a = 1, b = 1;
        if (iparr.length > 1) {//判断长度是否大于一，大于则为互联网系统交易量和tps
            List<Analysis> latps = analysisService.slcttps(fxbgtm, fxedtm, "hlwtps");//根据时间段查询tps
            List<Analysis> lasttps = analysisService.slcttps(lstfxbgtm, lstfxedtm, "hlwtps");//根据时间段查询上个月tps
            List<Analysis> lajyl = analysisService.slctonesys(fxbgtm, fxedtm, "hlwjyl");//根据时间段查询交易量
            List<Analysis> lastjyl = analysisService.slctonesys(lstfxbgtm, lstfxedtm, "hlwjyl");//根据时间段查询上个月交易量
            if(lastjyl == null || lasttps == null || lastjyl.size() == 0 || lasttps.size() == 0) {
                lastjyl = lajyl;
                lasttps = latps;
            }
            double c = 1, d = 1;
            for (int i = 0, lenth = latps.size(); i < lenth; i++) {

                    a = latps.get(i).getEntries();
                b = lasttps.get(i).getEntries();
                latps.get(i).setRatio(String.format("%.2f", (a / b - 1) * 100));
                c = lajyl.get(i).getEntries();
                d = lastjyl.get(i).getEntries();
                lajyl.get(i).setRatio(String.format("%.2f", (c / d - 1) * 100));
            }
            resultMap.put("latps", latps);
            resultMap.put("lajyl", lajyl);
        } else {
            List<Analysis> lajyl = analysisService.slctonesys(fxbgtm, fxedtm, sysaplatn);//本月交易量
            List<Analysis> lastjyl = analysisService.slctonesys(lstfxbgtm, lstfxedtm, sysaplatn);//上个月交易量
            lajyl.forEach(any -> timels.add(any.getJydate()));
            lajyl.forEach(any -> jlyls.add(any.getEntries()));
            if(lastjyl == null || lastjyl.size() == 0) {
                lastjyl = lajyl;
            }
            for (int i = 0, lenth = lajyl.size(); i < lenth; i++) {
                a = lajyl.get(i).getEntries();
                b = lastjyl.get(i).getEntries();
                lajyl.get(i).setRatio(String.format("%.2f", (a / b - 1) * 100));
            }
            resultMap.put("lajyl", lajyl);
            resultMap.put("timels", timels);
            resultMap.put("jlyls", jlyls);
        }
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");
        return resultMap;
    }
}
