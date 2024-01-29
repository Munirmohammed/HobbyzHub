package com.hobbyzhub.chatservice.listener;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebSockectChannelInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		/*
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		
		String userId = accessor.getFirstNativeHeader("client-id");
		
		log.info("Client with id: {} trying to connect", userId);
		log.info("Intercept connect event...");
		*/
		return ChannelInterceptor.super.preSend(message, channel);
	}
	
}
