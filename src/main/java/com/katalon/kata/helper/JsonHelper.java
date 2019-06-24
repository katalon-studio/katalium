package com.katalon.kata.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    private static class ObjectMapperHolder{
        private static final ObjectMapper INSTANCE = createInstance();

        private static ObjectMapper createInstance() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
        }
    }

    public static ObjectMapper objectMapper() {
        return ObjectMapperHolder.INSTANCE;
    }
}
