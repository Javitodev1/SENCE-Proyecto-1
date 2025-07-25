package com.core;

public class LoggingService {
    static String message;

    static void log(String message) {
        LoggingService.message = message;
        System.err.println(message);
    }
}
