package com.example.demo.model.mntrdtl;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "cpu")
public class Hostrst10 {

    @Column(name = "avgiw")
    private double avgiw;
    @Column(name = "avgus")
    private double avgus;

    public double getAvgiw() {
        return Double.parseDouble(String.format("%.2f", avgiw));
    }

    public void setAvgiw(double avgiw) {
        this.avgiw = avgiw;
    }

    public double getAvgus() {
        return Double.parseDouble(String.format("%.2f", avgus));
    }

    public void setAvgus(double avgus) {
        this.avgus = avgus;
    }
}
