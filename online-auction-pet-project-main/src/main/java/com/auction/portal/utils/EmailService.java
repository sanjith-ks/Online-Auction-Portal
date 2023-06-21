package com.auction.portal.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailService {
    @Autowired
    private JavaMailSender mail;

    public void normalMail(String toEmail, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("birunesh@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mail.send(message);
        log.info("Mail Sent to " + toEmail);
    }
}
