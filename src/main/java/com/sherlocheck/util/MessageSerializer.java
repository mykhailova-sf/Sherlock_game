package com.sherlocheck.util;

import com.sherlocheck.net.message.Message;

import java.io.*;
import java.util.Base64;

public class MessageSerializer {

    public static String serialize(Message message) {
        String serialized = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(message);
            oos.flush();

            serialized = Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serialized;
    }

    public static Message deserialize(String message) {
        Message messageObj = null;
        try {
            byte[] data = Base64.getDecoder().decode(message);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object obj = ois.readObject();

            if (obj instanceof Message) {
                messageObj = (Message) obj;
            }
        } catch (Exception e) {
            System.out.println("Deserialization error: " + e.getMessage());
        }

        return messageObj;
    }
}
