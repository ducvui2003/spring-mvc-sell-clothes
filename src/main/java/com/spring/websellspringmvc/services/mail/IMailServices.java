package com.spring.websellspringmvc.services.mail;

import jakarta.mail.MessagingException;

public interface IMailServices {
    void send() throws MessagingException;
}
