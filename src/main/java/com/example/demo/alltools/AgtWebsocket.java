package com.example.demo.alltools;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint(value = "/realagent/{ip}")

public class AgtWebsocket {

    private Session session = null;
    private String gpsn = null;
    private Integer linkCount = 0;


    private static CopyOnWriteArraySet<AgtWebsocket> webSocketSet = new CopyOnWriteArraySet<AgtWebsocket>();

    /**
     *      * 新的客户端连接调用的方法
     * <p>
     *      * @param session
     * <p>
     *     
     */


    @OnOpen

    public void onOpen(Session session,@PathParam("gpsn") String gpsn) throws IOException {

        System.out.println("-------------有新的客户端连接----------");
        linkCount++;
        this.gpsn = gpsn;
        this.session = session;
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

        try {

            sendMessage("发生变化" + message);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    /**
     *      * 发送信息调用的方法
     * <p>
     *      * @param message
     * <p>
     *      * @throws IOException
     * <p>
     *     
     * @param message
     */

    public static void sendMessage(String message) throws IOException {

        for (AgtWebsocket item : webSocketSet) {

            item.session.getBasicRemote().sendText(message);

        }

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

/**
 *  * socket连接
 * <p>
 *  
 */

