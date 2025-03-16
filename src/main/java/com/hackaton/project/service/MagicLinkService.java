package com.hackaton.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hackaton.project.model.MagicLink;
import com.hackaton.project.model.User;
import com.hackaton.project.repository.MagicLinkRepository;
import com.hackaton.project.repository.UserRepository;
import com.hackaton.project.security.JwtTokenUtil;

@Service
public class MagicLinkService {
    private final MagicLinkRepository magicLinkRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtTokenUtil jwtTokenUtil;
    private final int expirationMinutes;
    
    public MagicLinkService(
            MagicLinkRepository magicLinkRepository,
            UserRepository userRepository,
            EmailService emailService,
            JwtTokenUtil jwtTokenUtil,
            @Value("${magic.link.expiration-minutes:15}") int expirationMinutes) {
        this.magicLinkRepository = magicLinkRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.expirationMinutes = expirationMinutes;
    }
    
    @Transactional
    public void sendMagicLink(String email) {
        // Check if a valid link already exists for this email
        Optional<MagicLink> existingLink = magicLinkRepository
                .findByEmailAndUsedFalseAndExpirationTimeAfter(email, LocalDateTime.now());
        
        MagicLink magicLink = existingLink.orElseGet(() -> {
            MagicLink newLink = new MagicLink(email, expirationMinutes);
            return magicLinkRepository.save(newLink);
        });
        
        emailService.sendMagicLinkEmail(email, magicLink.getToken());
    }
    
    @Transactional
    public void registerWithMagicLink(String email) {
        // Check if user already exists
        boolean isNewRegistration = !userRepository.findByEmail(email).isPresent();
        
        if (isNewRegistration) {
            // Create a new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setEnabled(true);
            newUser.setProfileCompleted(false);
            userRepository.save(newUser);
        }
        
        // Send magic link for login
        sendMagicLink(email);
    }
    
    @Transactional
    public MagicLinkVerificationResult verifyMagicLink(String token) {
        MagicLink magicLink = magicLinkRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid magic link"));
        
        if (!magicLink.isValid()) {
            throw new BadCredentialsException("Magic link has expired or already been used");
        }
        
        // Mark the link as used
        magicLink.setUsed(true);
        magicLinkRepository.save(magicLink);
        
        // Get user
        User user = userRepository.findByEmail(magicLink.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        
        // Generate JWT token
        String jwtToken = jwtTokenUtil.generateToken(user);
        
        return new MagicLinkVerificationResult(
            jwtToken, 
            user.isProfileCompleted(), 
            !user.isProfileCompleted() // if profile not completed, it's likely a new registration
        );
    }
    
    // Scheduled task to clean up expired tokens
    @Scheduled(cron = "0 0 */6 * * *") // Run every 6 hours
    @Transactional
    public void cleanupExpiredTokens() {
        magicLinkRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
    
    // Inner class to return verification result
    public static class MagicLinkVerificationResult {
        private final String token;
        private final boolean profileCompleted;
        private final boolean newRegistration;
        
        public MagicLinkVerificationResult(String token, boolean profileCompleted, boolean newRegistration) {
            this.token = token;
            this.profileCompleted = profileCompleted;
            this.newRegistration = newRegistration;
        }
        
        public String getToken() {
            return token;
        }
        
        public boolean isProfileCompleted() {
            return profileCompleted;
        }
        
        public boolean isNewRegistration() {
            return newRegistration;
        }
    }
}
