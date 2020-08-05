package com.example.demo.dao;

import com.example.demo.model.Analysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnalysisDao {

    void setAnalysis(Analysis analysis) ;

    List<Analysis> slctonesys(String fxbgtm, String fxedtm, String sysaplatn);

    List<Analysis> slcttps(String fxbgtm, String fxedtm, String sysaplatn);
}
