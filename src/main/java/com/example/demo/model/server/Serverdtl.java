package com.example.demo.model.server;

public class Serverdtl {
    private String serverip;
    private String syscpu;
    private String sysmem;
    private String nodemc;
    private String checktime;

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }


    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public String getSyscpu() {
        return syscpu;
    }

    public void setSyscpu(String syscpu) {
        this.syscpu = syscpu;
    }

    public String getSysmem() {
        return sysmem;
    }

    public void setSysmem(String sysmem) {
        this.sysmem = sysmem;
    }

    public String getNodemc() {
        return nodemc;
    }

    public void setNodemc(String nodemc) {
        this.nodemc = nodemc;
    }
}
