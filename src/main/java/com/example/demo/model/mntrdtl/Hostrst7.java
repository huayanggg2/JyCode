package com.example.demo.model.mntrdtl;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "disk")
public class Hostrst7 {
    @Column(name = "path",tag = true)
    private String path;
    @Column(name = "disk_total")
    private long disk_total;
    @Column(name = "disk_uspt")
    private double disk_uspt;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getDisk_uspt() {
        return Double.parseDouble(String.format("%.2f", disk_uspt));
    }

    public void setDisk_uspt(double disk_uspt) {
        this.disk_uspt = disk_uspt;
    }

    public long getDisk_total() {
        return disk_total;
    }

    public void setDisk_total(long disk_total) {
        this.disk_total = disk_total;
    }
}
