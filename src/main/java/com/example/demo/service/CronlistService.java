package com.example.demo.service;

import com.example.demo.dao.CronlistDao;
import com.example.demo.model.Crondtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CronlistService {

    @Autowired
    CronlistDao cronlistDao;

    public void stopCron(int cronid) {
        cronlistDao.stopCron(cronid);
    }

    public List<Crondtl> getCrondtl() {
        return cronlistDao.getCrondtl();
    }

    public Crondtl getCronbycronid(int cronid) {
        return cronlistDao.getCronbycronid(cronid);
    }

    public void startCron(int cronid) {
        cronlistDao.startCron(cronid);
    }

    public List<Crondtl> getAllTasks(String find) {
        return cronlistDao.getAllTasks(find);
    }

    public void createTask(Crondtl crondtl) {
        cronlistDao.createTask(crondtl);
    }

    public void deleteTask(int cronid) {
        cronlistDao.deleteTask(cronid);
    }

    public void updateTask(Crondtl crondtl) {
        cronlistDao.updateTask(crondtl);
    }

    public List<Crondtl> showonetask(int cronid) {
        return cronlistDao.showonetask(cronid);
    }
}
