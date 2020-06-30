package com.example.demo.controller.crontabs;


import com.example.demo.CronTestConfiguration;
import com.example.demo.crondata.MyRunnable1;
import com.example.demo.crondata.MyRunnable2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * <pre>
 *    @author : orange
 *    @e-mail : 495314527@qq.com
 *    @time   : 2018/8/28 16:15
 *    @desc   : 定时任务
 *    @version: 1.0
 * </pre>
 */
@Slf4j
@RestController
@Api(description = "定时任务")
@RequestMapping("/quartz/task")
public class GetjyController {

    @Autowired
    private CronTestConfiguration cronTestConfiguration;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> future1;

    private ScheduledFuture<?> future2;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @PostMapping("/startCron1")
    @ApiOperation("开始定时任务1")
    public void startCron1() {

        future1 = threadPoolTaskScheduler.schedule(new MyRunnable1(),new Trigger(){
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext){
                return new CronTrigger(cronTestConfiguration.getCron1()).nextExecutionTime(triggerContext);
            }
        });

        System.out.println("DynamicTask.startCron1()");


    }

    @PostMapping("/stopCron1")
    @ApiOperation("关闭定时任务1")
    public String stopCron1() {
        if (future1 != null) {
            future1.cancel(true);
        }
        System.out.println("DynamicTask.stopCron1()");
        return "";
    }


    @PostMapping("/startCron2")
    @ApiOperation("开始定时任务2")
    public String startCron2() {

        future2 = threadPoolTaskScheduler.schedule(new MyRunnable2(),new Trigger(){
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext){
                return new CronTrigger(cronTestConfiguration.getCron2()).nextExecutionTime(triggerContext);
            }
        });

        System.out.println("DynamicTask.startCron2()");

        return "";

    }

    @PostMapping("/stopCron2")
    @ApiOperation("关闭定时任务2")
    public String stopCron2() {
        if (future2 != null) {
            future2.cancel(true);
        }
        System.out.println("DynamicTask.stopCron2()");
        return "";
    }

}