package com.sherlocheck.net.message.common;

import com.sherlocheck.net.message.TextMessage;
import com.sherlocheck.net.message.client.ClientMessage;
import com.sherlocheck.net.message.server.ServerMessage;

public class ChatMessage extends TextMessage implements ClientMessage, ServerMessage {
    public ChatMessage(String content) {
        super(content);
    }

    public String getMessage() {
        return getContent();
    }
}
