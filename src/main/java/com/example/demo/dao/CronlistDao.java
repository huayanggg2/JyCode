package com.example.demo.dao;

import com.example.demo.model.Crondtl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CronlistDao {
    Crondtl getCronbycronid(int cronid);

    List<Crondtl> getCrondtl();

    void stopCron(int cronid);

    void startCron(int cronid);

    List<Crondtl> getAllTasks(String find);

    void createTask(Crondtl crondtl);

    void deleteTask(int cronid);

    void updateTask(Crondtl crondtl);

    List<Crondtl> showonetask(int cronid);
}
