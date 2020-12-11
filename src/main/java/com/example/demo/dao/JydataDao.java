package com.example.demo.dao;

import com.example.demo.model.jysys.Jydata;
import com.example.demo.model.jysys.Jydetail;
import com.example.demo.model.jysys.Jysystm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface JydataDao {
    List<Jydata> getAll();

    void setAll(Jydata jydata);

    List<Jydata> getBytime(String begtime, String endtime, String jysystm);

    List<Jydetail> getDetail(String dasn);

    void setSer(@Param("ljd") List<Jydetail> ljd);

    List<Jydetail> searchOne(String dasn, String serapi);

    List<Jysystm> getSystm();

    void addSystm(Jysystm jstm);

    List<String> getAllHost(String gpsn);
}
