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
                    String[] headerParts = readHeader(socket.getInputStream());
                    String serializationType = headerParts[0];
                    String contactListLabel = headerParts[1];
                    int dataSize = readDataSize(socket.getInputStream());
                    System.out.println(serializationType + " (" + contactListLabel + ") - Received " + dataSize + " bytes");
                } catch (IOException e) {
                    System.err.println("Error handling client connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Reads the header from the input stream and splits it into serialization type and contact list label.
     *
     * @param inputStream The input stream from the client.
     * @return An array where the first element is the serialization type and the second is the contact list label.
     * @throws IOException If an error occurs during reading.
     */
    private static String[] readHeader(InputStream inputStream) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        int b;
        while ((b = inputStream.read()) != -1 && b != '\n') {
            headerBuilder.append((char) b);
        }
        String header = headerBuilder.toString();
        return header.split(":", 2); // Split into two parts: type and number
    }

    /**
     * Reads the remaining data from the input stream to determine the size.
     *
     * @param inputStream The input stream from the client.
     * @return The total number of bytes read.
     * @throws IOException If an error occurs during reading.
     */
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
