package com.example.demo.model.mntrdtl;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "cpu")
public class Hostrst9 {

    @Column(name = "time")
    private long time;

    @Column(name = "cpu_user")
    private double cpu_user;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getCpu_user() {
        return Double.parseDouble(String.format("%.2f", cpu_user));
    }

    public void setCpu_user(double cpu_user) {
        this.cpu_user = cpu_user;
    }
}
