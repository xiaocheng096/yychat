package com.ycychat.model;

import java.io.Serializable;

/**
 * 客户端与服务器之间传输的消息对象。
 */
public class Message implements Serializable, MessageType {

    private static final long serialVersionUID = 1L;

    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}