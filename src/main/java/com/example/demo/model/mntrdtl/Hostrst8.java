package com.example.demo.model.mntrdtl;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "diskio")
public class Hostrst8 {

    @Column(name = "time")
    private long time;

    @Column(name = "io_avg")
    private double io_avg;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getIo_avg() {
        return Double.parseDouble(String.format("%.2f", io_avg));
    }

    public void setIo_avg(double io_avg) {
        this.io_avg = io_avg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name")
    private String name;
}
