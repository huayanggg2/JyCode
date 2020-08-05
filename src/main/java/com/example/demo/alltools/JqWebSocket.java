package com.example.demo.alltools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.MyEndpointConfigure;
import com.example.demo.crondata.MyRunnable;
import com.example.demo.crondata.MyRunnable1;
import com.example.demo.dao.JydataDao;
import com.example.demo.dao.MotorDao;
import com.example.demo.model.server.Esmfc;
import com.example.demo.service.MotorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledFuture;

/**
 *  * socket连接
 * <p>
 *  
 */

@Component
@ServerEndpoint(value = "/reales/{currentPage}/{pageSize}", configurator = MyEndpointConfigure.class)
public class JqWebSocket {

    @Autowired
    private ThreadPoolTaskScheduler tdptScheduler;

   @Autowired
   private MotorDao motorDao;

    private Session session = null;

    private Integer linkCount = 0;
    private int currentPage = 0;
    private int pageSize = 0;
    private ScheduledFuture<?> sdfuture;

    @Bean
    public ThreadPoolTaskScheduler tdptScheduler() {
        return new ThreadPoolTaskScheduler();
    }


    private static CopyOnWriteArraySet<JqWebSocket> webSocketSet = new CopyOnWriteArraySet<JqWebSocket>();

    /**
     *      * 新的客户端连接调用的方法
     * <p>
     *      * @param session
     * <p>
     *     
     */

    @OnOpen
    public void onOpen(Session session, @PathParam("currentPage") int currentPage, @PathParam("pageSize") int pageSize) throws IOException {

        System.out.println("-------------有新的客户端连接----------");
        System.out.println(currentPage + "," + pageSize);
        linkCount++;
        this.session = session;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        webSocketSet.add(this);
        PageHelper.startPage(this.currentPage, this.pageSize);
        List<Esmfc> lses = motorDao.selectAlles();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageInfo<Esmfc> pageInfo = new PageInfo<Esmfc>(lses);
        resultMap.put("lses", pageInfo.getList());
        resultMap.put("pages", pageInfo.getTotal());
        JSONObject ob = JSONObject.parseObject(JSON.toJSONString(resultMap));
        String message = ob.toString();
        ScheduledFuture<?> future = tdptScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, triggerContext -> {
            //获取定时触发器，这里可以每次从数据库获取最新记录，更新触发器，实现定时间隔的动态调整
            CronTrigger cronTrigger = new CronTrigger("*/5 * * * * *");
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }

    /**
     *      * 收到客户端消息后调用的方法
     * <p>
     *      * @param message
     * <p>
     *     
     */

    @OnMessage
    public void onMessage(String message) {

        System.out.println("发生变化" + message);

    }


    /**
     *      * 发送信息调用的方法
     * <p>
     *      * @param message
     * <p>
     *      * @throws IOException
     * <p>
     */


    public void sendMessage(String message) throws IOException {

        for (JqWebSocket item : webSocketSet) {

            item.session.getBasicRemote().sendText(message);

        }

    }


    @OnClose
    public void onClose() {

//thread1.stopMe();

        linkCount--;
        if(sdfuture != null) {
            sdfuture.cancel(true);
        }
        webSocketSet.remove(this);

    }


    @OnError
    public void onError(Session session, Throwable error) {

        System.out.println("发生错误");

        error.printStackTrace();

    }

}