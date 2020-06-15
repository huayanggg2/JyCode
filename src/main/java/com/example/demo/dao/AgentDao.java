package com.example.demo.dao;

import com.example.demo.model.Agentpmfc;
import com.example.demo.model.Agentsystm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentDao {
    List<Agentsystm> selectsystem();

    List<Agentpmfc> selectBysystm(String gpsn);

    List<Agentpmfc> selectByIp(String[] iparr);

    List selectCpu(String hostip, String period);

    List selectMem(String hostip, String period);

    List<String> selectAllip(String gpsn);
}
