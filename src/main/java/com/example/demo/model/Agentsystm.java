package com.example.demo.model;

import java.util.List;

public class Agentsystm {
    private int id;
    private String sysgp;
    private String gpsn;
    private int ipcnt;
    private int runproces;

    public int getRunproces() {
        return runproces;
    }

    public void setRunproces(int runproces) {
        this.runproces = runproces;
    }

    public int getIpcnt() {
        return ipcnt;
    }

    public void setIpcnt(int ipcnt) {
        this.ipcnt = ipcnt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSysgp() {
        return sysgp;
    }

    public void setSysgp(String sysgp) {
        this.sysgp = sysgp;
    }

    public String getGpsn() {
        return gpsn;
    }

    public void setGpsn(String gpsn) {
        this.gpsn = gpsn;
    }
}
