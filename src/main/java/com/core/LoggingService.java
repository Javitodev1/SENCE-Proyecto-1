package com.core;

public class LoggingService {
    public static String message;

    public static void log(String message) {
        LoggingService.message = message;
        System.err.println(message);
    }
}
