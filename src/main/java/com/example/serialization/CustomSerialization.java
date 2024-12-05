package com.example.serialization;
import java.io.*;
import com.example.model.Contact;
import com.example.model.ContactList;
import java.util.ArrayList;
import java.util.List;
public class CustomSerialization {

    public static byte[] serialize(ContactList contactList) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);

        out.writeInt(contactList.getContacts().size());
        for (Contact contact : contactList.getContacts()) {
            writeContact(out, contact);
        }
        out.flush();
        out.close();
        return bos.toByteArray();
    }

    public static ContactList deserialize(byte[] data) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        int size = in.readInt();
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            contacts.add(readContact(in));
        }

        return new ContactList(contacts);
    }

    private static void writeContact(DataOutputStream out, Contact contact) throws IOException {
        out.writeUTF(contact.getName() != null ? contact.getName() : "");
        out.writeUTF(contact.getEmail() != null ? contact.getEmail() : "");
    }

    private static Contact readContact(DataInputStream in) throws IOException {
        String name = in.readUTF();
        String email = in.readUTF();
        return new Contact(name, email);
    }
}
