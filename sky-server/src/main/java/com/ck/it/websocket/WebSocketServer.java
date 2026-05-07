package com.ck.it.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.ck.it.websocket
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/7 14:25
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {
	// 存放会话对象
	private static final Map<String, Session> sessionMap = new HashMap<>();

	/**
	 * 建立连接成功调用的方法
	 *
	 * @param session
	 * @param sid
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("sid") String sid) {
		System.out.println("客户端" + sid + "建立连接");
		sessionMap.put(sid, session);
	}

	/**
	 * 客户端的请求
	 *
	 * @param message
	 * @param sid
	 */
	@OnMessage
	public void onMessage(String message, @PathParam("sid") String sid) {
		System.out.println("收到来自客户端：" + sid + "的请求：" + message);
	}

	/**
	 * 关闭连接
	 *
	 * @param sid
	 */
	@OnClose
	public void onError(@PathParam("sid") String sid) {
		System.out.println("客户端：" + sid + "的连接关闭");
		sessionMap.remove(sid);
	}

	/**
	 * 发送到所有客户端
	 *
	 * @param message
	 */
	public void sendToAllClient(String message) {
		Collection<Session> sessions = sessionMap.values();
		for (Session session : sessions) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
