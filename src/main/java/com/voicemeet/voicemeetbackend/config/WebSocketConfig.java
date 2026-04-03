package com.voicemeet.voicemeetbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "http://localhost",                      // ✅ APK (MOST IMPORTANT)
                        "http://localhost:*",                   // ✅ APK dynamic ports
                        "http://127.0.0.1:*",                   // ✅ fallback
                        "https://voicemeet-frontend.onrender.com"
                )
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
}