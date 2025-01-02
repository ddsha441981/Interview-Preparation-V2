package cwc.interview.api.v2.config;

import cwc.interview.api.v2.handler.AudioWebSocketHandler;
import cwc.interview.api.v2.service.impl.AudioProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer  {
    private final AudioProcessingService audioProcessingService;

    @Autowired
    public WebSocketConfig(AudioProcessingService audioProcessingService) {
        this.audioProcessingService = audioProcessingService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(createWebSocketHandler(), "/ws/stream")
                .setAllowedOrigins("*"); // Replace with specific origins in production
    }

    private WebSocketHandler createWebSocketHandler() {
        return new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                        if (session.getBinaryMessageSizeLimit() < 150000) { // Setting a larger buffer size limit
                            session.setBinaryMessageSizeLimit(150000); // Increase the buffer size
                        }
                        if (session.getTextMessageSizeLimit() < 150000) {
                            session.setTextMessageSizeLimit(150000); // Increase the buffer size
                        }
                        super.handleMessage(session, message);
                    }
                };
            }
        }.decorate(new AudioWebSocketHandler(audioProcessingService));
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new AudioWebSocketHandler(audioProcessingService), "/ws/stream")
//                .setAllowedOrigins("*"); // Replace with specific origins in production
//    }




//    private final AudioStreamHandler audioStreamHandler;
//
//    public WebSocketConfig(AudioStreamHandler audioStreamHandler) {
//        this.audioStreamHandler = audioStreamHandler;
//    }
//
//
//
//        @Override
//        public void configureMessageBroker(MessageBrokerRegistry registry) {
//            registry.enableSimpleBroker("/topic");
//            registry.setApplicationDestinationPrefixes("/app");
//        }
//
//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
//        registry.setMessageSizeLimit(2097152); // Example: 2MB
//        registry.setSendBufferSizeLimit(2097152); // Example: 2MB
//        registry.setSendTimeLimit(20 * 10000); // Example: 20 seconds
//    }
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws/stream")
//                .setAllowedOrigins("http://localhost:63342")
//                .withSockJS();
//    }
//
//    @Bean
//    public WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory() {
//        return new WebSocketHandlerDecoratorFactory() {
//            @Override
//            public WebSocketHandlerDecorator decorate(WebSocketHandler handler) {
//                return new WebSocketHandlerDecorator(handler) {
//                    @Override
//                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//                        super.afterConnectionEstablished(session);
//                        log.info("WebSocket connection established with session id: {}", session.getId());
//                    }
//
//                    @Override
//                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//                        super.handleMessage(session, message);
//                        log.info("Received message: {}", message.getPayload());
//                    }
//
//                    @Override
//                    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//                        super.afterConnectionClosed(session, status);
//                        log.info("WebSocket connection closed with session id: {} and status: {}", session.getId(), status);
//                    }
//                };
//            }
//        };
    }

//    @Bean
//    public WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory() {
//        return new WebSocketHandlerDecoratorFactory() {
//            @Override
//            public WebSocketHandler decorate(WebSocketHandler handler) {
//                return new WebSocketHandlerDecorator(handler) {
//                    @Override
//                    public void afterPropertiesSet() throws Exception {
//                        super.afterPropertiesSet();
//                        if (handler instanceof AbstractWebSocketMessageBrokerConfigurer) {
//                            ((AbstractWebSocketMessageBrokerConfigurer) handler)
//                                    .setMaxMessageSize(1048576); // Set desired buffer size, e.g., 1MB
//                        }
//                    }
//                };
//            }
//        };
//    }
//        @Override
//        public void registerStompEndpoints(StompEndpointRegistry registry) {
//            registry.addEndpoint("/ws/stream").setAllowedOrigins("http://localhost:63342").withSockJS();
//        }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(audioStreamHandler, "/stream")
//                .setAllowedOrigins("http://localhost:63342").addInterceptors(new HttpSessionHandshakeInterceptor());
////                .withSockJS();
//    }


//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new TextWebSocketHandler() {
//            @Override
//            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//                log.info("WebSocket connection established with session id: {}", session.getId());
//            }
//
//            @Override
//            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//                log.info("Received message: {} from session id: {}", message.getPayload(), session.getId());
//            }
//
//            @Override
//            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//                log.error("WebSocket transport error in session id: {}", session.getId(), exception);
//            }
//
//            @Override
//            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//                log.info("WebSocket connection closed with session id: {} and close status: {}", session.getId(), closeStatus);
//            }
//
//            @Override
//            public boolean supportsPartialMessages() {
//                return false;
//            }
//        }, "/stream").setAllowedOrigins("http://localhost:63342").withSockJS();
//    }



