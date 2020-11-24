package com.example.demo.model.mntrdtl;

import com.example.demo.alltools.Jisuan;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "mem")
public class Hostrst2 {
    @Column(name = "mem_total")
    private long mem_total;

        public long getMem_total() {
            mem_total = mem_total/1024/1024/1024;
        return mem_total;
    }

    public void setMem_total(long mem_total) {
        this.mem_total = mem_total;
    }
}
