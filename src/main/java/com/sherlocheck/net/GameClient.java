package com.sherlocheck.net;

import com.sherlocheck.game.Game;
import com.sherlocheck.game.player.Player;
import com.sherlocheck.net.message.Message;
import com.sherlocheck.net.message.client.ClientMessage;
import com.sherlocheck.net.message.server.ClientRoleResponse;
import com.sherlocheck.util.SceneManager;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class GameClient extends BaseGameConnection {

    public void connect(String host, int port) throws IOException {
        System.out.println("Connecting to " + host + ":" + port);
        if (connection == null) {
            Socket socket = new Socket(host, port);
            System.out.println("Connected to Server!!");
            connection = new Connection(socket);
            connection.setMessageHandler(this::onMessageReceived);
        } else {
            System.out.println("Already connected!");
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        super.onMessageReceived(message);

        System.out.println("Received from Server: " + message);

        if (message instanceof ClientRoleResponse) {
            Player.ROLE role = ((ClientRoleResponse) message).getClientRole();
            Game.setPlayerRole(role);
            Platform.runLater(() -> {
                SceneManager.switchScene("/MainScene.fxml");
            });
        }
    }

    @Override
    boolean isAcceptableMessageTypeToSend(Message message) {
        return message instanceof ClientMessage;
    }
}
