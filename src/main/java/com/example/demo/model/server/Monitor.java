package com.example.demo.model.server;

public class Monitor {
    private String serverip;
    private String nodemc;
    private String[][] cktcpu;
    private String[][] cktmem;

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public String getNodemc() {
        return nodemc;
    }

    public void setNodemc(String nodemc) {
        this.nodemc = nodemc;
    }

    public String[][] getCktcpu() {
        return cktcpu;
    }

    public void setCktcpu(String[][] cktcpu) {
        this.cktcpu = cktcpu;
    }

    public String[][] getCktmem() {
        return cktmem;
    }

    public void setCktmem(String[][] cktmem) {
        this.cktmem = cktmem;
    }
}
