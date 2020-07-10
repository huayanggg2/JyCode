package com.example.demo.model.agent;

import java.util.List;

public class Hostdtl {
    private int id;
    private String hgpsn;
    private String hostip;
    private String csdeip;
    private String agentvsn;
    private String system;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHgpsn() {
        return hgpsn;
    }

    public void setHgpsn(String hgpsn) {
        this.hgpsn = hgpsn;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    public String getCsdeip() {
        return csdeip;
    }

    public void setCsdeip(String csdeip) {
        this.csdeip = csdeip;
    }

    public String getAgentvsn() {
        return agentvsn;
    }

    public void setAgentvsn(String agentvsn) {
        this.agentvsn = agentvsn;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
