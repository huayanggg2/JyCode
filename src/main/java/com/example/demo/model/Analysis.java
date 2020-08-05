package com.example.demo.model;

public class Analysis {
    private String jydate;
    private String sysaplatn;
    private String type;
    private int entries;
    private String ratio;

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getJydate() {
        return jydate;
    }

    public void setJydate(String jydate) {
        this.jydate = jydate;
    }

    public String getSysaplatn() {
        return sysaplatn;
    }

    public void setSysaplatn(String sysaplatn) {
        this.sysaplatn = sysaplatn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }
}
