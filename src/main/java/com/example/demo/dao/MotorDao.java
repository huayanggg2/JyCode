package com.example.demo.dao;

import com.example.demo.model.Motor;
import com.example.demo.model.server.Copnts;
import com.example.demo.model.server.Serverdtl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MotorDao {
    List<Motor> getmonitor();

    List<Serverdtl> getMonitorBytime(String begintime, String endtime);

    List<Serverdtl> getcpuByip(String ahostip, String begintime, String endtime);

    List<Serverdtl> getmemByip(String ahostip, String begintime, String endtime);

    List<Copnts> getallnode();
}
