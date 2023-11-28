package org.csq.websocket;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单实现websocket机制：
 * 首先加入相关依赖
 * 然后通过配置类注入ServerEndpointExporter对象开启springboot的websocket功能
 * 该server类提供websocket事件处理方法
 * websocket.html作为测试前端，可以开启两个实例扮演id为1的用户和id为2的用户进行聊天
 *
 * 注：本项目集成了security，会对请求进行拦截，因此需要在security的配置类中进行对应请求的放行websocket方可正常访问
 */
@Component
@ServerEndpoint(value = "/websocket/{userId}")
public class WebsocketServer {

    public static final Map<String,Session> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        sessionMap.put(userId,session);
        session.getBasicRemote().sendText("连接成功");
    }

    @OnClose
    public void onClose(Session session,@PathParam("userId") String userId) throws IOException {
        sessionMap.remove(userId);
        System.out.println("连接断开");
        session.close();
    }

    @OnMessage
    public void onMessage(String message,Session session,@PathParam("userId") String userId) throws IOException {
        System.out.println("客户端发送的消息：" + message);
        if (userId.equals("1")){
            userId = "2";
        }else {
            userId = "1";
        }
        Session sendToSession = sessionMap.get(userId);
        if (ObjectUtils.isEmpty(sendToSession)){
            session.getBasicRemote().sendText(userId + "用户不在线");
        }else {
            sendToSession.getBasicRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }
}
