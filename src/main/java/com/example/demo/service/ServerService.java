package com.example.demo.service;

import com.example.demo.dao.ServerDao;
import com.example.demo.model.server.Copnts;
import com.example.demo.model.server.Serverdtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    public String name;
    @Autowired
    ServerDao serverDao;

    public List<Copnts> getCopnts() {
        return serverDao.getCopnts();
    }

    public List<Serverdtl> getServerdtl() {
        return serverDao.getServerdtl();
    }
}
