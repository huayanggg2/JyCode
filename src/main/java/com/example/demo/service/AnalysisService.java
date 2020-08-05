package com.example.demo.service;

import com.example.demo.dao.AnalysisDao;
import com.example.demo.model.Analysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisDao analysisDao;
    public List<Analysis> slctonesys(String fxbgtm, String fxedtm, String sysaplatn) {
        return analysisDao.slctonesys(fxbgtm,fxedtm,sysaplatn);
    }

    public List<Analysis> slcttps(String fxbgtm, String fxedtm, String sysaplatn) {
        return analysisDao.slcttps(fxbgtm,fxedtm,sysaplatn);
    }
}
