package com.ashish.websocket.Model;

import java.util.Base64;

public class Message {
    private String type;
    private String username;
    private String message;
    private Object offer; // Changed to String for SDP handling
    private Object answer; // Changed to String for SDP handling
    private Object candidate; // Changed to String for ICE candidate handling
    private String fileName; // For file transfer
    private byte[] fileData; // For file transfer (binary data)
    private Integer currentChunk;

    // Getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getOffer() {
        return offer;
    }

    public void setOffer(Object offer) {
        this.offer = offer;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public Object getCandidate() {
        return candidate;
    }

    public void setCandidate(Object candidate) {
        this.candidate = candidate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    // Set fileData as base64 encoded string
    public void setFileData(String base64FileData) {

        // System.out.println(base64FileData);
        // Decode base64 string to byte array
        this.fileData = Base64.getDecoder().decode(base64FileData);
    }

    public Integer getCurrentChunk() {
        return currentChunk;
    }

    public void setCurrentChunk(Integer currentChunk) {
        this.currentChunk = currentChunk;
    }
}
