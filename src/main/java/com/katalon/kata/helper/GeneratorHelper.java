package com.katalon.kata.helper;

import java.util.UUID;

public class GeneratorHelper {
    public static String generateUniqueValue() {
        return UUID.randomUUID().toString();
    }
}
