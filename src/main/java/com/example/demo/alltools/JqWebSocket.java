package com.example.demo.alltools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.server.Esmfc;
import com.example.demo.service.MotorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *  * socket连接
 * <p>
 *  
 */


@ServerEndpoint(value = "/reales/{currentPage}/{pageSize}")
public class JqWebSocket {
    @Autowired
    MotorService motorService;

    private Session session = null;

    private Integer linkCount = 0;
    private int currentPage = 0;
    private int pageSize = 0;

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
        System.out.println(currentPage + "," +pageSize);
        linkCount++;

        this.session = session;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        webSocketSet.add(this);
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

        /*try {

            sendMessage("发生变化" + message);

        } catch (Exception e) {

            e.printStackTrace();
        }*/

    }


    /**
     *      * 发送信息调用的方法
     * <p>
     *      * @param message
     * <p>
     *      * @throws IOException
     * <p>
     *
     */

    public void sendMessage() throws IOException {
        /*PageHelper.startPage(this.currentPage, this.pageSize);
        List<Esmfc> lses = motorService.selectAlles();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageInfo<Esmfc> pageInfo = new PageInfo<Esmfc>(lses);
        resultMap.put("lses", pageInfo.getList());
        JSONObject ob = JSONObject.parseObject(JSON.toJSONString(resultMap));
        String str = ob.toString();
        for (JqWebSocket item : webSocketSet) {
            item.session.getBasicRemote().sendText(str);
            System.out.println(str);
        }*/

    }


    @OnClose

    public void onClose() {

//thread1.stopMe();

        linkCount--;

        webSocketSet.remove(this);

    }


    @OnError

    public void onError(Session session, Throwable error) {

        System.out.println("发生错误");

        error.printStackTrace();

    }

}