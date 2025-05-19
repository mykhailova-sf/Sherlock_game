package com.sherlocheck.net;

import com.sherlocheck.net.message.detective.Answer;
import com.sherlocheck.net.message.Message;
import com.sherlocheck.net.message.common.ChatMessage;

import java.util.function.Consumer;

public interface GameConnection {
    void onMessageReceived(Message message);

    void setAnswerHandler(Consumer<Answer> handler);

    void close();

    void sendMessage(Message message);


    void setQuestionHandler(Consumer<String> questionHandler);

    void setChatMessageHandler(Consumer<ChatMessage> handler);

    void setTimeoutHandler(Runnable timeoutHandler);

    void setGameOverHandler(Runnable gameOverHandler);
}
