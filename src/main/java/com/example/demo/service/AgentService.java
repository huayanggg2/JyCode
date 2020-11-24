package com.example.demo.service;

import com.example.demo.dao.AgentDao;
import com.example.demo.model.agent.Agentpmfc;
import com.example.demo.model.agent.Agentsystm;
import com.example.demo.model.agent.Hostdtl;
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

    public List<Agentpmfc> selectByIp(String[] iparr,String gpsn) {
        return agentDao.selectByIp(iparr,gpsn);
    }

    public List<Agentpmfc> selectCpu(String hostip, int period) {
        return agentDao.selectCpu(hostip,period);
    }

    public List<Agentpmfc> selectMem(String hostip, int period) {
        return agentDao.selectMem(hostip,period);
    }

    public List<String> selectAllip(String gpsn) {
        return agentDao.selectAllip(gpsn);
    }

    public void setCpuwarn(String gpsn, String cpuValue) {
        agentDao.setCpuwarn(gpsn,cpuValue);
    }


    public void addhost(Hostdtl hostdtl) {
        agentDao.addhost(hostdtl);
    }
}
