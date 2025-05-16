package com.sherlocheck.net;
import com.sherlocheck.net.message.Message;
import com.sherlocheck.net.message.MessageHandler;
import com.sherlocheck.util.MessageSerializer;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Connection {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private MessageHandler handler;

    private Consumer<Message> messageHandler;

    public void setMessageHandler(Consumer<Message> handler) {
        this.messageHandler = handler;
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.messageHandler = m -> System.out.println("Empty handler in Connection");

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);

        startListening();
    }

    private void startListening() {
        Thread thread = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    messageHandler.accept(MessageSerializer.deserialize(message));
                }
            } catch (IOException e) {
                System.out.println("Connection closed: " + e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() throws IOException {
        socket.close();
    }
}
