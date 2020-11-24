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
        //前端需要，所以写的很复杂
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] iparr = sysaplatn.split(",");
        List<String> timels = new ArrayList<>();
        List<Integer> jlyls = new ArrayList<>();
        double a = 1, b = 1;
        if (iparr.length > 1) {
            List<Analysis> latps = analysisService.slcttps(fxbgtm, fxedtm, "hlwtps");
            List<Analysis> lasttps = analysisService.slcttps(lstfxbgtm, lstfxedtm, "hlwtps");
            List<Analysis> lajyl = analysisService.slctonesys(fxbgtm, fxedtm, "hlwjyl");
            List<Analysis> lastjyl = analysisService.slctonesys(lstfxbgtm, lstfxedtm, "hlwjyl");
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
            List<Analysis> lajyl = analysisService.slctonesys(fxbgtm, fxedtm, sysaplatn);
            List<Analysis> lastjyl = analysisService.slctonesys(lstfxbgtm, lstfxedtm, sysaplatn);
            lajyl.forEach(any -> timels.add(any.getJydate()));
            lajyl.forEach(any -> jlyls.add(any.getEntries()));
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
