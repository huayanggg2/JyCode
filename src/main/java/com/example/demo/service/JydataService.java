package com.example.demo.service;

import com.example.demo.dao.JydataDao;
import com.example.demo.model.jysys.Jydata;
import com.example.demo.model.jysys.Jydetail;
import com.example.demo.model.jysys.Jysystm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JydataService {
    @Autowired
    JydataDao jydataDao;

    public List<Jydata> getAll() {
        return jydataDao.getAll();
    }

    public void setAll(Jydata jd) {
        jydataDao.setAll(jd);
    }

    public List<Jydata> getBytime(String begtime, String endtime, String jysystm) {
        return jydataDao.getBytime(begtime, endtime, jysystm);
    }

    public List<Jydetail> getDetail(String dasn) {
        return jydataDao.getDetail(dasn);
    }

    public List<Jydetail> searchOne(String dasn, String serapi) {
        return jydataDao.searchOne(serapi, dasn);
    }

    public List<Jysystm> getSystm() {
        return jydataDao.getSystm();
    }

    public void setSer(List<Jydetail> ljd) {
        jydataDao.setSer(ljd);
    }
}
