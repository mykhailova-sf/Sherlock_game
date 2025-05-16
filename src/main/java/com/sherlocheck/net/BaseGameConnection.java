package com.sherlocheck.net;

import com.sherlocheck.game.Game;
import com.sherlocheck.game.Round;
import com.sherlocheck.game.player.Player;
import com.sherlocheck.net.message.common.GameOver;
import com.sherlocheck.net.message.detective.Answer;
import com.sherlocheck.net.message.Message;
import com.sherlocheck.net.message.detective.Timeout;
import com.sherlocheck.net.message.storyteller.Question;
import com.sherlocheck.net.message.common.ChatMessage;
import com.sherlocheck.util.MessageSerializer;
import com.sherlocheck.util.SceneManager;
import javafx.application.Platform;

import java.io.IOException;
import java.util.function.Consumer;

abstract class BaseGameConnection implements GameConnection {

    protected Connection connection;
    protected Consumer<ChatMessage> chatMessageHandler;

    private Consumer<String> questionHandler;

    private Consumer<Answer> answerHandler = answer -> {
        System.out.println("Detective has decided story is " + answer.getContent());
    };

    private Runnable timeoutHandler;

    @Override
    public void setQuestionHandler(Consumer<String> questionHandler) {
        this.questionHandler = questionHandler;
    }

    @Override
    public void setChatMessageHandler(Consumer<ChatMessage> handler) {
        System.out.println("setChatMessageHandler: " + this.getClass().getName());
        chatMessageHandler = handler;
    }

    @Override
    public void setAnswerHandler(Consumer<Answer> handler) {
        answerHandler = handler;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        if (!isAcceptableMessageTypeToSend(message) && !isGameDialog(message)) {
            throw new IllegalArgumentException("I cannot send a message of type " + message.getClass().getName());
        }

        connection.sendMessage(MessageSerializer.serialize(message));
    }

    private boolean isGameDialog(Message message) {
        return message instanceof Question
                || message instanceof Answer
                || message instanceof Timeout
                || message instanceof GameOver;
    }

    @Override
    public void onMessageReceived(Message message) {
        System.out.println("Received from SUPER: " + message);

        if (message instanceof ChatMessage chatMessage) {
            getChatMessageHandler().accept(chatMessage);
        }

        Player.ROLE role = Game.getCurrentRole();

        if (role == null) {
            return;
        }

        if (
                role == Player.ROLE.DETECTIVE
                        && message instanceof Question question
                && questionHandler != null
        ) {
            questionHandler.accept(question.getContent());
            Game.setNewRound(new Round(question.isAffirmationIsTrue()));
        }

        if (
                role == Player.ROLE.STORYTELLER
                && message instanceof Answer answer
                && answerHandler != null
        ) {
            answerHandler.accept(answer);
        }
        if (
                role == Player.ROLE.STORYTELLER
                && message instanceof Timeout
                && timeoutHandler != null
        ) {
            Game.incrementStorytellerCount();
            timeoutHandler.run();
        }
        if (message instanceof GameOver) {
            Platform.runLater(
                    () -> SceneManager.switchScene("/StartScene.fxml")
            );
        }
    }

    protected Consumer<ChatMessage> getChatMessageHandler() {
        if (chatMessageHandler == null) {
            System.out.println(" NULLL getChatMessageHandler: " + this.getClass().getName());
        }

        return chatMessageHandler;
    }

    abstract boolean isAcceptableMessageTypeToSend(Message message);

    @Override
    public void setTimeoutHandler(Runnable timeoutHandler) {
        this.timeoutHandler = timeoutHandler;
    }
}
