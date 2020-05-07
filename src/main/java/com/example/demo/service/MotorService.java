package com.example.demo.service;

import com.example.demo.dao.JydataDao;
import com.example.demo.dao.MotorDao;
import com.example.demo.model.Motor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MotorService {
    @Autowired
    MotorDao motorDao;
    public List<Motor> getmonitor() {
        return motorDao.getmonitor();
    }
}
