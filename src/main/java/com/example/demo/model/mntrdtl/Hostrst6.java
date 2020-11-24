package com.example.demo.model.mntrdtl;

import com.example.demo.alltools.Jisuan;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "net")
public class Hostrst6 {
    @Column(name = "time")
    private long time;

    public long getBytes_recv() {
        return bytes_recv;
    }

    public void setBytes_recv(long bytes_recv) {
        this.bytes_recv = bytes_recv;
    }

        public long getBytes_sent() {
        return bytes_sent;
    }

    public void setBytes_sent(long bytes_sent) {
        this.bytes_sent = bytes_sent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Column(name = "bytes_recv")
    private long bytes_recv;

    @Column(name = "bytes_sent")
    private long bytes_sent;

    @Column(name = "interface")
    private String intfc;

    public String getIntfc() {
        return intfc;
    }

    public void setIntfc(String intfc) {
        this.intfc = intfc;
    }
}
