package com.example.demo.model.mntrdtl;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "system")
public class Hostrst3 {
    @Column(name = "time")
    private long time;
    
    @Column(name = "load1")
    private double load1;

    @Column(name = "load5")
    private double load5;

    @Column(name = "load15")
    private double load15;

}
