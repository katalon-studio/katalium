package com.katalon.kata.helper;

import org.slf4j.Logger;

public class ExceptionHelper {

    private static final Logger log = LogHelper.getLogger();

    public static <T> T rethrow(Exception e) {
        log.info("Rethrow exception: {} {}", e.getClass().getName(), e.getMessage());
        throw new IllegalStateException(e);
    }
}
