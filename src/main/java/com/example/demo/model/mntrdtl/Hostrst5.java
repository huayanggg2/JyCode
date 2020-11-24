package com.example.demo.model.mntrdtl;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;


@Measurement(name = "mem")
public class Hostrst5 {
    @Column(name = "used_5m")
    private double used_5m;

    public double getUsed_5m() {
        return Double.parseDouble(String.format("%.2f", used_5m));
    }

    public void setUsed_5m(double used_5m) {
        this.used_5m = used_5m;
    }
}
