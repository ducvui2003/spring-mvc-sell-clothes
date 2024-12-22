package com.spring.websellspringmvc.services.mail;

import com.spring.websellspringmvc.properties.MailProperties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MailLostKey implements IMailServices {
    private String emailTo;
    private String username;
    private String tokenLostKey;

    public MailLostKey(String emailTo, String username, String tokenLostKey) {
        this.emailTo = emailTo;
        this.username = username;
        this.tokenLostKey = tokenLostKey;
    }

    @Override
    public void send() throws MessagingException {
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailProperties.getEmail(), MailProperties.getPassword());
            }
        };

//        Create Session
        Session session = Session.getInstance(MailProperties.getProperties(), auth);
//        Create message
        Message message = new MimeMessage(session);
//        From
        message.setFrom(new InternetAddress(MailProperties.getEmail()));
//        To
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false));
//        Subject
        String subjectMess = "Thông báo xác nhận mất khóa";
        message.setSubject(subjectMess);
//        Set no reply
        message.setReplyTo(null);
//        Content
        InputStream is = MailProperties.class.getClassLoader().getResourceAsStream("templates/templateLostKey.html");
        String htmlTemplate = htmlTemplate(is);
        message.setContent(htmlTemplate, "text/html; charset = UTF-8");

//        Send mail
        ThreadMail.executorService.execute(() -> {
            try {
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String htmlTemplate(InputStream is) {
        String template = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;

        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            template += line + "\n";
        }
        template = template.replace("%%USERNAME%%", username);
        String[] tokenLostKeySplit = tokenLostKey.split("");
        for (int i = 0; i < tokenLostKeySplit.length; i++) {
            template = template.replace("%%VALUE" + (i + 1) + "%%", tokenLostKeySplit[i]);
        }
        return template;
    }
}
