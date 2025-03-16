package com.hackaton.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final String baseUrl;
    
    public EmailService(@Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public void sendMagicLinkEmail(String email, String token) {
        String magicLink = baseUrl + "/api/auth/verify-magic-link?token=" + token;
        
        // In a real application, you would send an actual email
        // For now, we'll just log it
        logger.info("Sending magic link to {}: {}", email, magicLink);
        
        // Example code for sending an actual email:
        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Magic Login Link");
        message.setText("Click the link below to sign in:\n\n" + magicLink + 
                "\n\nThis link will expire in 15 minutes.");
        mailSender.send(message);
        */
    }
}