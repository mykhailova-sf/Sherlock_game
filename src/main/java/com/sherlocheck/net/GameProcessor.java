package com.sherlocheck.net;

public interface GameProcessor {
    void start();

    boolean isActive();
    void sendMessage(String message);
}
