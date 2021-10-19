package com.pj.project4sp.user4notify;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.BiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/notification/{userId}", encoders = { ServerEncoder.class })
@Component
public class NotificationSocket {

    private static Logger logger = LogManager.getLogger(NotificationSocket.class.getName());

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    private static CopyOnWriteArraySet<NotificationSocket> wsClientMap = new CopyOnWriteArraySet<>();

    private static BiMap<Long, NotificationSocket> wsClientMap = new BiMap<>(new ConcurrentHashMap<>());

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;


    /**
     * 连接建立成功调用的方法
     * @param session 当前会话session
     */
    @OnOpen
    public void onOpen (@PathParam("userId") Long userId, Session session){
//        long userId = (long) StpUtil.getLoginIdByToken(satoken);
        this.session = session;
        wsClientMap.put(userId, this);
        addOnlineCount();
        logger.info(session.getId()+"有新链接加入，当前链接数为：" + wsClientMap.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose (){
        wsClientMap.getInverse().remove(this);
        subOnlineCount();
        logger.info("有一链接关闭，当前链接数为：" + wsClientMap.size());
    }

    /**
     * 收到客户端消息
     * @param message 客户端发送过来的消息
     * @param session 当前会话session
     * @throws IOException
     */
    @OnMessage
    public void onMessage (String message, Session session) throws IOException {
        logger.info("来终端的警情消息:" + message);
        sendMsgToAll(message);
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("wsClientMap发生错误!");
        error.printStackTrace();
    }

    /**
     * 给所有客户端群发消息
     * @param message 消息内容
     */
    public void sendMsgToAll(String message) {
        wsClientMap.forEach((userId, item) -> {
            try {
                item.session.getBasicRemote().sendText(message);
            } catch (IOException ignored) {}
        });
//        for ( NotificationSocket item : wsClientMap ){
//            item.session.getBasicRemote().sendText(message);
//        }
        logger.info("成功群送一条消息:" + wsClientMap.size());
    }

    public void sendMessage (String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        logger.info("成功发送一条消息:" + message);
    }

    public static void sendMsg(Long userId, String message) throws IOException {
        NotificationSocket notificationSocket = wsClientMap.get(userId);
        notificationSocket.session.getBasicRemote().sendText(message);

        logger.info("成功群送一条消息:" + wsClientMap.size());
    }

    public static void sendObjMsg(Long userId, Object message) throws IOException, EncodeException {
        NotificationSocket notificationSocket = wsClientMap.get(userId);
        notificationSocket.session.getBasicRemote().sendObject(message);

        logger.info("成功群送一条消息:" + wsClientMap.size());
    }

    public static Boolean checkOnline(Long userId) {
        NotificationSocket notificationSocket = wsClientMap.get(userId);
        if (BeanUtil.isEmpty(notificationSocket)) return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public static synchronized  int getOnlineCount (){
        return NotificationSocket.onlineCount;
    }

    public static synchronized void addOnlineCount (){
        NotificationSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount (){
        NotificationSocket.onlineCount--;
    }
}