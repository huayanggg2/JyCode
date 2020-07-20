package com.example.demo.service;

import com.example.demo.dao.MotorDao;
import com.example.demo.model.server.Copnts;
import com.example.demo.model.server.Esmfc;
import com.example.demo.model.server.Serverdtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorService {
    @Autowired
    MotorDao motorDao;

    public List<Serverdtl> getMonitorBytime(String begintime, String endtime) {
        return motorDao.getMonitorBytime(begintime, endtime);
    }

    public List<Serverdtl> getcpuByip(String ahostip, String begintime, String endtime) {
        return motorDao.getcpuByip(ahostip,begintime,endtime);
    }

    public List<Serverdtl> getmemByip(String ahostip, String begintime, String endtime) {
        return motorDao.getmemByip(ahostip,begintime,endtime);
    }

    public List<Copnts> getallnode() {
        return motorDao.getallnode();
    }

    public List<Esmfc> selectAlles() {
        return motorDao.selectAlles();
    }

    public List<Esmfc> selectesByIp(String[] iparr) {
        return motorDao.selectesByIp(iparr);
    }
}
