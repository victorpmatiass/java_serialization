package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 50000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    String serializationType = readSerializationType(socket.getInputStream());
                    int dataSize = readDataSize(socket.getInputStream());
                    System.out.println(serializationType + " - Received " + dataSize + " bytes");
                }
            }
        }
    }

    private static String readSerializationType(InputStream inputStream) throws IOException {
        StringBuilder typeBuilder = new StringBuilder();
        int b;
        while ((b = inputStream.read()) != -1 && b != '\n') {
            typeBuilder.append((char) b);
        }
        return typeBuilder.toString();
    }

    private static int readDataSize(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024 * 16];
        int bytesRead;
        int totalBytesRead = 0;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            totalBytesRead += bytesRead;
        }
        return totalBytesRead;
    }
}
