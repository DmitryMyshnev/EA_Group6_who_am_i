package com.eleks.academy.whoami.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
}
