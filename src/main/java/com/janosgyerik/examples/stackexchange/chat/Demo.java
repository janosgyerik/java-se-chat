package com.janosgyerik.examples.stackexchange.chat;

import java.io.UnsupportedEncodingException;

public class Demo {
    private static final String EMAIL = "your email";
    private static final String PASSWORD = "your password";
    private static final int ROOM_ID = 26245;  // RoboSanta's Playground
    private static final String MESSAGE = "Hello World!";

    public static void main(String[] args) throws UnsupportedEncodingException {
        Client client = new Client();
        client.login(EMAIL, PASSWORD);
        client.sendMessage(ROOM_ID, MESSAGE);
    }
}
