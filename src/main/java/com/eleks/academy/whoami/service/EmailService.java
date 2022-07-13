package com.eleks.academy.whoami.service;

import org.springframework.mail.MailException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text) throws MailException;
}
