package com.example.demo.model.mntrdtl;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "system")
public class Hostrst1 {
 /*   @Column(name = "time")
    private double time;

    @Column(name = "host",tag = true)
    private String host;
*/
    @Column(name = "cpu_load5")
    private double cpu_load5;

    @Column(name = "n_cpus")
    private double n_cpus;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
