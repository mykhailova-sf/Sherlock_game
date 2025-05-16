package com.sherlocheck.net;

import com.sherlocheck.game.Game;

import java.io.IOException;

public class Starter {

    public static final int SERVER_PORT = 5010;

    public static void startServer(Runnable postHandler) {
        new Thread(() -> {
            try {
                GameServer gameServer = new GameServer();
                gameServer.start(SERVER_PORT);
                Game.setGameConnection(gameServer);

                postHandler.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void startClient(String host) {
        try {
            GameClient gameClient = new GameClient();
            gameClient.connect(host, SERVER_PORT);
            Game.setGameConnection(gameClient);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
