package com.sherlocheck.net;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class GameDiscovery {

    public static ArrayList<String> discover(int port) {
        String subnet;
        ArrayList<String> addresses = new ArrayList<>();

        addresses.add("localhost");

        return addresses;
/*

        try {
            subnet = getSubnet();
        } catch (SocketException e) {
            return new ArrayList<>();
        }

        for (int i = 1; i < 255; i++) {
            String host = subnet + i;
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 200);
                System.out.println("There is game on: " + host);
                addresses.add(host);
            } catch (IOException ignored) {
            }
        }

        System.out.println("Net scanning ended");

        return addresses;*/
    }

    private static String getSubnet() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                    String ip = addr.getHostAddress();
                    return ip.substring(0, ip.lastIndexOf('.') + 1); // пример: "192.168.0."
                }
            }
        }
        return "192.168.1."; // fallback
    }

}

