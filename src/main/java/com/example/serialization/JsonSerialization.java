package com.example.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonSerialization {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        // Exclude null and default values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        // Disable writing type identifiers and extra whitespace
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false); 
    }

    public static byte[] serialize(Object obj) throws IOException {
        return objectMapper.writeValueAsBytes(obj);
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        return objectMapper.readValue(data, clazz);
    }
}
