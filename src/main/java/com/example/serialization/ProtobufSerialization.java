package com.example.serialization;

import com.example.protobuf.ContactList;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtobufSerialization {
    public static byte[] serialize(ContactList contactList) {
        return contactList.toByteArray();
    }

    public static ContactList deserialize(byte[] data) throws InvalidProtocolBufferException {
        return ContactList.parseFrom(data);
    }
}
