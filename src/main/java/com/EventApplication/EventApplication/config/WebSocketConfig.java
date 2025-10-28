package com.EventApplication.EventApplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Registrerar en WebSocket-endpoints som klienter (t.ex. Angular, React) kan ansluta till.
        // "/ws" blit URL:en där anslutningen sker, ex: ws://localhost:8080/ws
        registry.addEndpoint("/ws")
                //Tillåter all klienter från andra domäner (CORS) får genom att använda "*"
                .setAllowedOriginPatterns("*")
                // Aktiverar SockJS-stöd - en fallback lösning om WebSocket inte stöds i klienten
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // prefix för meddelande som skickas från klienten till server
        //Klienter lyssnar på /topic/
        registry.enableSimpleBroker("/topic");
        //klienter skickar till /app/
        registry.setApplicationDestinationPrefixes("/app");
    }

}
