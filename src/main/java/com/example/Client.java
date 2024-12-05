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
import java.util.ArrayList;
import java.util.List;

public class Client {
    // Configurable variables for list sizes
    private static final int SINGLE_CONTACT_COUNT = 1;
    private static final int MULTIPLE_CONTACT_COUNT = 10000;

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 50000;

        // Optionally, allow list sizes to be set via command-line arguments
        int singleCount = SINGLE_CONTACT_COUNT;
        int multipleCount = MULTIPLE_CONTACT_COUNT;

        if (args.length >= 2) {
            try {
                singleCount = Integer.parseInt(args[0]);
                multipleCount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid arguments. Using default list sizes.");
            }
        }

        // Create ContactList instances based on the defined sizes
        ContactList singleContactList = createContactList(singleCount);
        ContactList multipleContactList = createContactList(multipleCount);

        System.out.println("Sending single ContactList (" + singleCount + " contact(s)):");
        testSerializationMethods(singleContactList, host, port, "Single (" + singleCount + ")");

        System.out.println("\nSending ContactList with " + multipleCount + " Contacts:");
        testSerializationMethods(multipleContactList, host, port, "List of " + multipleCount);
    }

    /**
     * Creates a ContactList with the specified number of Contacts.
     *
     * @param num The number of Contact objects to include.
     * @return A ContactList instance containing the specified number of Contacts.
     */
    private static ContactList createContactList(int num) {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            contacts.add(new Contact("Name" + i, "email" + i + "@example.com"));
        }
        return new ContactList(contacts);
    }

    /**
     * Tests all serialization methods by serializing and sending the ContactList.
     *
     * @param contactList      The ContactList to serialize and send.
     * @param host             The server host.
     * @param port             The server port.
     * @param contactListLabel A label indicating the type of ContactList (e.g., "Single", "List of 100").
     * @throws Exception If an error occurs during serialization or sending.
     */
    private static void testSerializationMethods(ContactList contactList, String host, int port, String contactListLabel) throws Exception {
        // Java Default Serialization
        byte[] javaData = DefaultSerialization.serialize(contactList);
        sendData(javaData, host, port, "Java Default Serialization", contactList.getContacts().size(), contactListLabel);

        // Custom Serialization
        byte[] customData = CustomSerialization.serialize(contactList);
        sendData(customData, host, port, "Custom Serialization", contactList.getContacts().size(), contactListLabel);

        // JSON Serialization
        byte[] jsonData = JsonSerialization.serialize(contactList);
        sendData(jsonData, host, port, "JSON Serialization", contactList.getContacts().size(), contactListLabel);

        // XML Serialization
        byte[] xmlData = XmlSerialization.serialize(contactList);
        sendData(xmlData, host, port, "XML Serialization", contactList.getContacts().size(), contactListLabel);

        // Protobuf Serialization
        byte[] protoData = ProtobufSerialization.serialize(convertToProto(contactList));
        sendData(protoData, host, port, "Protobuf Serialization", contactList.getContacts().size(), contactListLabel);
    }

    /**
     * Converts a ContactList to its Protobuf equivalent.
     *
     * @param contactList The ContactList to convert.
     * @return A Protobuf ContactList object.
     */
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

    /**
     * Sends serialized data to the server with a header indicating the serialization type and number of contacts.
     *
     * @param data              The serialized data to send.
     * @param host              The server host.
     * @param port              The server port.
     * @param serializationType The type of serialization used.
     * @param numContacts       The number of Contact objects in the ContactList.
     * @param contactListLabel  A label indicating the type of ContactList (e.g., "Single", "List of 100").
     * @throws IOException If an error occurs during sending.
     */
    private static void sendData(byte[] data, String host, int port, String serializationType, int numContacts, String contactListLabel) throws IOException {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream()) {
            // Header format: SerializationType:NumberOfContacts\n
            String header = serializationType + ":" + numContacts + "\n";
            outputStream.write(header.getBytes("UTF-8"));
            outputStream.write(data);
            outputStream.flush();
            System.out.println(serializationType + " (" + contactListLabel + ") - Sent " + data.length + " bytes");
        }
    }
}
