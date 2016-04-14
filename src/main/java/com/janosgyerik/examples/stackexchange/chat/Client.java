package com.janosgyerik.examples.stackexchange.chat;

import java.io.UnsupportedEncodingException;

public class Client {

    private final Browser browser;

    public Client() {
        browser = new Browser();
    }

    public void login(String email, String password) throws UnsupportedEncodingException {
        browser.loginToStackExchange(email, password);
        browser.loginToSite();
        browser.loginToChat();
    }

    public void sendMessage(int roomID, String text) throws UnsupportedEncodingException {
        browser.sendMessage(roomID, text);
    }
}
