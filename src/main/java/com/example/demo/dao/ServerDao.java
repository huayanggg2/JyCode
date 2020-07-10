package com.example.demo.dao;

import com.example.demo.model.server.Copnts;
import com.example.demo.model.server.Serverdtl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServerDao {
    List<Copnts> getCopnts();

    List<Serverdtl> getServerdtl();
}
