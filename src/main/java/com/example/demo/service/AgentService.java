package com.example.demo.service;

import com.example.demo.dao.AgentDao;
import com.example.demo.model.Agentpmfc;
import com.example.demo.model.Agentsystm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AgentService {
    @Autowired
    AgentDao agentDao;
    public List<Agentsystm> selectsystem() {

        return agentDao.selectsystem();
    }

    public List<Agentpmfc> selectBysystm(String gpsn) {
        return agentDao.selectBysystm(gpsn);
    }

    public List<Agentpmfc> selectByIp(String[] iparr) {
        return agentDao.selectByIp(iparr);
    }

    public List selectCpu(String hostip, String period) {
        return agentDao.selectCpu(hostip,period);
    }

    public List selectMem(String hostip, String period) {
        return agentDao.selectMem(hostip,period);
    }

    public List<String> selectAllip(String gpsn) {
        return agentDao.selectAllip(gpsn);
    }
}
