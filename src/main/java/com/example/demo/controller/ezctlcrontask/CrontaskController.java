package com.example.demo.controller.ezctlcrontask;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.crondata.MyRunnable;
import com.example.demo.model.Crondtl;
import com.example.demo.model.server.Esmfc;
import com.example.demo.service.CronlistService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Controller
public class CrontaskController implements ApplicationRunner {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private CronlistService cronlistService;

    private Map<Integer, ScheduledFuture<?>> futuresMap;

    /**
     * 在ScheduledFuture中有一个cancel可以停止定时任务。
     */
    private ScheduledFuture<?> future;

    @PostConstruct
    public void init() {
        futuresMap = new ConcurrentHashMap<>();
    }


    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Crondtl> lctls = cronlistService.getCrondtl();

        lctls.forEach(task -> {
            //任务执行线程
            Runnable runnable = new MyRunnable(task);
            //任务触发器
            Trigger trigger = triggerContext -> {
                //获取定时触发器，这里可以每次从数据库获取最新记录，更新触发器，实现定时间隔的动态调整
                CronTrigger cronTrigger = new CronTrigger(task.getCrontk());
                return cronTrigger.nextExecutionTime(triggerContext);
            };

            //注册任务
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(runnable, trigger);
            ScheduledFuture oldScheduledFuture = futuresMap.put(task.getCronid(), future);
            if (oldScheduledFuture == null) {
                System.out.println(("添加定时任务成功:" + task.getCronkey()));
            } else {
                throw new RuntimeException("添加任务key名： " + task.getCronkey() + "重复");
            }

        });

    }

    @ResponseBody
    @RequestMapping("/crons/stopTask")
    public String stopTask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        int cronid = ob.getJSONObject("bizContent").getInteger("cronid");
        cronlistService.stopCron(cronid);
        ScheduledFuture toBeRemovedFuture = futuresMap.remove(cronid);
        if (toBeRemovedFuture != null) {
            toBeRemovedFuture.cancel(true);
            return "stopTask";
        } else {
            return "false";
        }
    }

    @ResponseBody
    @RequestMapping("/crons/deleteTask")
    public Map<String, Object> deleteTask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int cronid = ob.getJSONObject("bizContent").getInteger("cronid");
        cronlistService.deleteTask(cronid);
        if (futuresMap.get(cronid) != null) {
            futuresMap.get(cronid).cancel(true);
            futuresMap.remove(cronid);
        }
        resultMap.put("status", "0000");
        resultMap.put("message", "成功");

        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/crons/startTask")
    public String addTask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        int cronid = ob.getJSONObject("bizContent").getInteger("cronid");
        Crondtl crondtl = cronlistService.getCronbycronid(cronid);
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(crondtl), triggerContext -> {
            //获取定时触发器，这里可以每次从数据库获取最新记录，更新触发器，实现定时间隔的动态调整
            CronTrigger cronTrigger = new CronTrigger(crondtl.getCrontk());
            return cronTrigger.nextExecutionTime(triggerContext);
        });
        ScheduledFuture oldScheduledFuture = futuresMap.put(crondtl.getCronid(), future);
        cronlistService.startCron(cronid);
        if (oldScheduledFuture == null) {
            System.out.println(("添加定时任务成功:" + crondtl.getCronid()));
            return "添加成功";
        } else {
            throw new RuntimeException("添加任务key名： " + crondtl.getCronid() + "重复");
        }
    }

    @ResponseBody
    @RequestMapping("/crons/createTask")
    public String createTask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        Crondtl crondtl = new Crondtl();
        crondtl.setCrontk(ob.getJSONObject("bizContent").getString("crontk"));
        crondtl.setSystm(ob.getJSONObject("bizContent").getString("systm"));
        crondtl.setGetapi(ob.getJSONObject("bizContent").getString("getapi"));
        crondtl.setCronkey(ob.getJSONObject("bizContent").getString("cronkey"));
        crondtl.setUsername(ob.getJSONObject("bizContent").getString("username"));
        crondtl.setStatus(0);
        cronlistService.createTask(crondtl);
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(crondtl), triggerContext -> {
            //获取定时触发器，这里可以每次从数据库获取最新记录，更新触发器，实现定时间隔的动态调整
            CronTrigger cronTrigger = new CronTrigger(crondtl.getCrontk());
            return cronTrigger.nextExecutionTime(triggerContext);
        });
        ScheduledFuture oldScheduledFuture = futuresMap.put(crondtl.getCronid(), future);
        return "createTask";
    }

    @ResponseBody
    @RequestMapping("/crons/updateTask")
    public Map<String, Object> updateTask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Crondtl crondtl = new Crondtl();
        int cronid = ob.getJSONObject("bizContent").getInteger("cronid");
        crondtl.setCrontk(ob.getJSONObject("bizContent").getString("crontk"));
        crondtl.setCronid(cronid);
        crondtl.setSystm(ob.getJSONObject("bizContent").getString("systm"));
        crondtl.setGetapi(ob.getJSONObject("bizContent").getString("getapi"));
        crondtl.setCronkey(ob.getJSONObject("bizContent").getString("cronkey"));
        crondtl.setUsername(ob.getJSONObject("bizContent").getString("username"));
        if (futuresMap.get(cronid) != null) {
            ScheduledFuture toBeRemovedFuture = futuresMap.remove(cronid);
            if (toBeRemovedFuture != null) {
                toBeRemovedFuture.cancel(true);
            }
            cronlistService.updateTask(crondtl);
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(crondtl), triggerContext -> {
                //获取定时触发器，这里可以每次从数据库获取最新记录，更新触发器，实现定时间隔的动态调整
                CronTrigger cronTrigger = new CronTrigger(crondtl.getCrontk());
                return cronTrigger.nextExecutionTime(triggerContext);
            });
            ScheduledFuture oldScheduledFuture = futuresMap.put(crondtl.getCronid(), future);
            if (oldScheduledFuture == null) {
                System.out.println(("修改定时任务成功:" + crondtl.getCronid()));
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                return resultMap;
            } else {
                throw new RuntimeException("修改任务key名： " + crondtl.getCronid() + "重复");
            }
        } else {
            cronlistService.updateTask(crondtl);
            resultMap.put("status", "0000");
            resultMap.put("message", "成功");
            return resultMap;
        }

    }

    @ResponseBody
    @RequestMapping("/crons/showTask")
    public Map<String, Object> showTask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        int currentPage = ob.getJSONObject("bizContent").getInteger("currentPage");
        int pageSize = ob.getJSONObject("bizContent").getInteger("pageSize");
        String find = ob.getJSONObject("bizContent").getString("find");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Crondtl> alltasks = cronlistService.getAllTasks(find);
            PageInfo<Crondtl> pageInfo = new PageInfo<Crondtl>(alltasks);
            if (alltasks.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                //resultMap.put("alleslst", alleslst);
                resultMap.put("crontask", pageInfo.getList());
                resultMap.put("pages", pageInfo.getTotal());
            } else {
                resultMap.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", -1);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/crons/showonetask")
    public Map<String, Object> showonetask(@RequestBody String json) {
        JSONObject ob = JSONObject.parseObject(json);
        int cronid = ob.getJSONObject("bizContent").getInteger("cronid");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Crondtl> onetask = cronlistService.showonetask(cronid);
            if (onetask.size() > 0) {
                resultMap.put("status", "0000");
                resultMap.put("message", "成功");
                //resultMap.put("alleslst", alleslst);
                resultMap.put("onetask", onetask);
            } else {
                resultMap.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", -1);
        }
        return resultMap;
    }
}
