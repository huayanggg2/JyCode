package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/8/5.
 */
@Data
@Component
@ConfigurationProperties(prefix="crontest")
@EqualsAndHashCode(callSuper = false)
public class CronTestConfiguration {
    private String cron1;

    public String getCron1() {
        return cron1;
    }

    public void setCron1(String cron1) {
        this.cron1 = cron1;
    }

    public String getCron2() {
        return cron2;
    }

    public void setCron2(String cron2) {
        this.cron2 = cron2;
    }

    private String cron2;
}