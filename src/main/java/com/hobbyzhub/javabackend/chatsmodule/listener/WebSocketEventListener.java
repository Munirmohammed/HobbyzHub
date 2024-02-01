package com.hobbyzhub.javabackend.chatsmodule.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketEventListener {
	@EventListener(classes = SessionConnectEvent.class)
	public void handleSessionConnected(SessionConnectEvent event) {
		
		log.info("Fired session connected event...");
	}
	
	@EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {

    }
}
