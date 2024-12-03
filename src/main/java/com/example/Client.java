package com.example;

import com.example.model.Contact;
import com.example.model.ContactList;
import com.example.serialization.CustomSerialization;
import com.example.serialization.DefaultSerialization;
import com.example.serialization.JsonSerialization;
import com.example.serialization.XmlSerialization;
import com.example.serialization.ProtobufSerialization;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws Exception {
        ContactList contactList = createContactList();
        String host = "localhost";
        int port = 50000;

        testSerializationMethods(contactList, host, port);
    }

    private static ContactList createContactList() {
        return new ContactList(Arrays.asList(
            new Contact("Alice", "alice@example.com"),
            new Contact("Bob", "bob@example.com"),
            new Contact("Carol", "carol@example.com"),
            new Contact("Dave", "dave@example.com"),
            new Contact("Eve", "eve@example.com")
        ));
    }

    private static void testSerializationMethods(ContactList contactList, String host, int port) throws Exception {
        // Java Default Serialization
        byte[] javaData = DefaultSerialization.serialize(contactList);
        sendData(javaData, host, port, "Java Default Serialization");
        // Custom Serialization
        byte[] customData = CustomSerialization.serialize(contactList);
        sendData(customData, host, port, "Custom Serialization");
        // JSON Serialization
        byte[] jsonData = JsonSerialization.serialize(contactList);
        sendData(jsonData, host, port, "JSON Serialization");
        // XML Serialization
        byte[] xmlData = XmlSerialization.serialize(contactList);
        sendData(xmlData, host, port, "XML Serialization");
        // Protobuf Serialization
        byte[] protoData = ProtobufSerialization.serialize(convertToProto(contactList));
        sendData(protoData, host, port, "Protobuf Serialization");
    }

    private static com.example.protobuf.ContactList convertToProto(ContactList contactList) {
        com.example.protobuf.ContactList.Builder builder = com.example.protobuf.ContactList.newBuilder();
        for (Contact contact : contactList.getContacts()) {
            builder.addContacts(
                com.example.protobuf.Contact.newBuilder()
                    .setName(contact.getName())
                    .setEmail(contact.getEmail())
                    .build()
            );
        }
        return builder.build();
    }

    private static void sendData(byte[] data, String host, int port, String serializationType) throws IOException {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream()) {
            String header = serializationType + "\n";
            outputStream.write(header.getBytes("UTF-8"));
            outputStream.write(data);
            outputStream.flush();
            System.out.println(serializationType + " - Sent " + data.length + " bytes");
        }
    }
}
