package com.sherlocheck.net;

import com.sherlocheck.game.Game;
import com.sherlocheck.game.player.Player;
import com.sherlocheck.net.message.Message;
import com.sherlocheck.net.message.client.ClientRoleRequest;
import com.sherlocheck.net.message.common.ChatMessage;
import com.sherlocheck.net.message.server.ClientRoleResponse;
import com.sherlocheck.net.message.server.ServerMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends BaseGameConnection {
    public void start(int port) throws IOException {
        if (connection == null) {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for connection ...");
            Socket socket = serverSocket.accept();
            System.out.println("Server connected!");

            connection = new Connection(socket);
            connection.setMessageHandler(this::onMessageReceived);

        } else {
            System.out.println("Already connected!");
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        super.onMessageReceived(message);

        if (message instanceof ClientRoleRequest) {
            System.out.println("Received msg from client: " + message.getClass().getName());
            Player.ROLE clientRole = Game.getCurrentRole() == Player.ROLE.DETECTIVE
                    ? Player.ROLE.STORYTELLER
                    : Player.ROLE.DETECTIVE;
            sendMessage(new ClientRoleResponse(clientRole));
        }
    }

    @Override
    boolean isAcceptableMessageTypeToSend(Message message) {
        return message instanceof ServerMessage;
    }
}
