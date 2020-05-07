package com.example.demo.dao;

import com.example.demo.model.Motor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MotorDao {
    List<Motor> getmonitor();
}
