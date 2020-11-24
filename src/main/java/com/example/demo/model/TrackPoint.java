package com.example.demo.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "monitors")
public class TrackPoint {
    @Column(name = "time")
    private double time;

    @Column(name = "host",tag = true)
    private String host;

    @Column(name = "systm",tag = true)
    private String systm;

    @Column(name = "cpu")
    private float cpu;

    @Column(name = "mem")
    private float mem;

    @Column(name = "vxstatus")
    private String vxstatus;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}