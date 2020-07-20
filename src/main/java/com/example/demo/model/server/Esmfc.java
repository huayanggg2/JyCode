package com.example.demo.model.server;

public class Esmfc {
    private String acsdeip;//通信ip
    private String ahostip;//管理Ip
    private String esstatus;//探针进程状态
    private String sysdisk;//系统磁盘
    private String syscpu;//系统cpu
    private String sysmem;//系统内存

    public String getAcsdeip() {
        return acsdeip;
    }

    public void setAcsdeip(String acsdeip) {
        this.acsdeip = acsdeip;
    }

    public String getAhostip() {
        return ahostip;
    }

    public void setAhostip(String ahostip) {
        this.ahostip = ahostip;
    }

    public String getEsstatus() {
        return esstatus;
    }

    public void setEsstatus(String esstatus) {
        this.esstatus = esstatus;
    }

    public String getSysdisk() {
        return sysdisk;
    }

    public void setSysdisk(String sysdisk) {
        this.sysdisk = sysdisk;
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
}
