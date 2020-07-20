package com.example.demo.model.server;

public class Monitor {
    private String serverip;
    private String nodemc;
    private double[][] cktcpu;
    private double[][] cktmem;


    public double[][] getCktcpu() {
        return cktcpu;
    }

    public void setCktcpu(double[][] cktcpu) {
        this.cktcpu = cktcpu;
    }

    public double[][] getCktmem() {
        return cktmem;
    }

    public void setCktmem(double[][] cktmem) {
        this.cktmem = cktmem;
    }

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



}
