package com.sherlocheck.net.message;

abstract public class TextMessage implements Message {
    protected String content;

    public TextMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
