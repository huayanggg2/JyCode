package com.example.demo.model.mntrdtl;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "mem")
public class Hostrst2 {
    @Column(name = "mem_total")
    private Long mem_total;

        public long getMem_total() {
            double dbmem = mem_total.doubleValue()/1024/1024/1024;
            mem_total = (long) Math.ceil(dbmem);
        return mem_total;
    }

    public void setMem_total(long mem_total) {
        this.mem_total = mem_total;
    }
}
