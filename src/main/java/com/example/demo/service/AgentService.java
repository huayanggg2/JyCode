package com.example.demo.service;

import ch.ethz.ssh2.Connection;
import com.example.demo.alltools.Jshell;
import com.example.demo.dao.AgentDao;
import com.example.demo.model.agent.Agentpmfc;
import com.example.demo.model.agent.Agentsystm;
import com.example.demo.model.agent.Hostdtl;
import com.example.demo.model.agent.Sshhost;
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

   /* public List<Agentpmfc> selectBysystm(String gpsn) {
        return agentDao.selectBysystm(gpsn);
    }*/

    public List<Agentpmfc> selectByIp(String[] iparr, String gpsn) {
        return agentDao.selectByIp(iparr, gpsn);
    }

    public List<Agentpmfc> selectCpu(String hostip, int period) {
        return agentDao.selectCpu(hostip, period);
    }

    public List<Agentpmfc> selectMem(String hostip, int period) {
        return agentDao.selectMem(hostip, period);
    }

    public List<String> selectAllip(String gpsn) {
        return agentDao.selectAllip(gpsn);
    }

    public void setCpuwarn(String gpsn, String cpuValue) {
        agentDao.setCpuwarn(gpsn, cpuValue);
    }


    public void addhost(Hostdtl hostdtl) {
        agentDao.addhost(hostdtl);
    }

    public List<Hostdtl> selectifHost(String csdeip) {
        return agentDao.selectifHost(csdeip);
    }

    public void deleteSystm(String gpsn) {
        List<String> hostlst = agentDao.selectAllip(gpsn);
        if (hostlst.size() > 0) {
            for (int i = 0, lsz = hostlst.size(); i < lsz; i++) {
                Sshhost sshhost = new Sshhost();
                Jshell jshell = new Jshell();
                Connection conn = null;
                sshhost.setUsername("log");
                sshhost.setPassword("log");
                sshhost.setHostip(hostlst.get(i));
                conn = jshell.login(sshhost);
                String cmd = "sh /home/log/loginsight-agent/killvx.sh > /dev/null 2>&1";
                String result = jshell.execute(conn, cmd);
                System.out.println(result);
            }
        }
        agentDao.deleteSystm(gpsn);
        agentDao.deleteIps(gpsn);
    }
}
