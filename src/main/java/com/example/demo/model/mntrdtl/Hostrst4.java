package com.example.demo.model.mntrdtl;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "mem")
public class Hostrst4 {
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Column(name = "time")
    private long time;

    @Column(name = "used_percent")
    private double used_percent;

    public double getUsed_percent() {
        return Double.parseDouble(String.format("%.2f", used_percent));
    }

    public void setUsed_percent(double used_percent) {
        this.used_percent = used_percent;
    }
}
