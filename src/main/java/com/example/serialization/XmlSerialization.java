package com.example.serialization;

import com.example.model.ContactList;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException; // Adicione esta linha para importar IOException

public class XmlSerialization {
    
    public static byte[] serialize(ContactList contactList) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        // Configurar para saída compacta
        xmlMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, false);
        return xmlMapper.writeValueAsBytes(contactList);
    }

    public static ContactList deserialize(byte[] data) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(data, ContactList.class);
    }
}
