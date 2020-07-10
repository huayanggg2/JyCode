package com.example.demo.model.jysys;

public class Jysystm {
    private int id;
    private String sysgp;
    private String gpsn;
    private String cpuValue;
    private boolean ifdsab;

    public boolean isIfdsab() {
        return ifdsab;
    }

    public void setIfdsab(boolean ifdsab) {
        this.ifdsab = ifdsab;
    }

    public String getCpuValue() {
        return cpuValue;
    }

    public void setCpuValue(String cpuValue) {
        this.cpuValue = cpuValue;
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
