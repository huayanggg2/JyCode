package com.example.demo.dao;

import com.example.demo.model.agent.Agentpmfc;
import com.example.demo.model.agent.Agentsystm;
import com.example.demo.model.agent.Hostdtl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentDao {
    List<Agentsystm> selectsystem();

    List<Agentpmfc> selectBysystm(String gpsn);

    List<Agentpmfc> selectByIp(String[] iparr,String gpsn);

    List<Agentpmfc> selectCpu(String hostip, int period);

    List<Agentpmfc> selectMem(String hostip, int period);

    List<String> selectAllip(String gpsn);

    void setCpuwarn(String gpsn, String cpuValue);

    void addhost(Hostdtl hostdtl);

    List<Hostdtl> selectifHost(String csdeip);

    void deleteSystm(String gpsn);

    void deleteIps(String gpsn);
}
