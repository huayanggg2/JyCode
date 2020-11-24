package com.example.demo.config;



import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBConfig {

    @Value("${spring.influx.url}")
    private String url;

    private String database;

    private String retentionPolicy;

    private InfluxDB influxDB;

    public InfluxDBConfig() {
    }

    public InfluxDBConfig(String database) {
        this.database = database;
        build();
    }

    public InfluxDBConfig(String url, String database) {
        this.url = url;
        this.database = database;
        build();
    }

    private void build(){
        if(influxDB == null){
            influxDB = InfluxDBFactory.connect(this.url);
        }
        influxDB.setDatabase(this.database);
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }
}

