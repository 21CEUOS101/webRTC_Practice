package com.ashish.websocket.Utils;

import com.ashish.websocket.Model.Message;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SignalingHandler extends TextWebSocketHandler {

    private Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        rooms.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>()).put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String roomId = getRoomId(session);
        Map<String, WebSocketSession> roomSessions = rooms.get(roomId);

        if (roomSessions != null) {
            
            Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);
            switch (message.getType()) {
                case "join":
                    handleJoinMessage(session, message, roomSessions);
                    break;
                case "offer":
                case "answer":
                    handleSdpMessage(session, message, roomSessions);
                    break;
                case "candidate":
                    handleIceCandidate(session, message, roomSessions);
                    break;
                case "message":
                    handleTextChat(session, message, roomSessions);
                    break;
                case "file":
                    handleFileTransfer(session, message, roomSessions);
                    break;
                default:
                    // Handle unrecognized message type
                    break;
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getRoomId(session);
        Map<String, WebSocketSession> roomSessions = rooms.get(roomId);

        if (roomSessions != null) {
            roomSessions.remove(session.getId());
            if (roomSessions.isEmpty()) {
                rooms.remove(roomId);
            }
        }
    }

    private void handleJoinMessage(WebSocketSession session, Message message, Map<String, WebSocketSession> roomSessions) throws Exception {
        session.getAttributes().put("username", message.getUsername());
        for (WebSocketSession s : roomSessions.values()) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }
        }
    }

    private void handleSdpMessage(WebSocketSession session, Message message, Map<String, WebSocketSession> roomSessions) throws Exception {
        for (WebSocketSession s : roomSessions.values()) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }
        }
    }

    private void handleIceCandidate(WebSocketSession session, Message message, Map<String, WebSocketSession> roomSessions) throws Exception {
        for (WebSocketSession s : roomSessions.values()) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }
        }
    }

    private void handleTextChat(WebSocketSession session, Message message, Map<String, WebSocketSession> roomSessions) throws Exception {
        for (WebSocketSession s : roomSessions.values()) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }
        }
    }

    private void handleFileTransfer(WebSocketSession session, Message message, Map<String, WebSocketSession> roomSessions) throws Exception {
        for (WebSocketSession s : roomSessions.values()) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }
        }
    }

    private String getRoomId(WebSocketSession session) {
        return session.getUri().getPath().split("/signal/")[1];
    }
}
