package com.academy.fintech.origination.core.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String messageFrom;

    public void sendEmail(String email, String text) {
        log.info("To the email address :" + email + " were sent an email with this text: " + text);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(messageFrom);
//        message.setTo(email);
//        message.setText(text);
//
//        javaMailSender.send(message);
    }
}
