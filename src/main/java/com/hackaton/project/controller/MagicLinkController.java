package com.hackaton.project.controller;

import com.hackaton.project.dto.EmailRequest;
import com.hackaton.project.dto.JwtResponse;
import com.hackaton.project.service.MagicLinkService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class MagicLinkController {
    private static final Logger logger = LoggerFactory.getLogger(MagicLinkController.class);
    private final MagicLinkService magicLinkService;
    
    public MagicLinkController(MagicLinkService magicLinkService) {
        this.magicLinkService = magicLinkService;
    }
    
    @PostMapping("/request-magic-link")
    public ResponseEntity<?> requestMagicLink(@Valid @RequestBody EmailRequest request) {
        try {
            magicLinkService.sendMagicLink(request.getEmail());
            return ResponseEntity.ok(Map.of("message", "Magic link sent to your email"));
        } catch (Exception e) {
            logger.error("Error sending magic link", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to send magic link: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody EmailRequest request) {
        try {
            magicLinkService.registerWithMagicLink(request.getEmail());
            return ResponseEntity.ok(Map.of("message", "Registration successful. Magic link sent to your email"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/verify-magic-link")
    public ResponseEntity<?> verifyMagicLink(@RequestParam String token) {
        try {
            MagicLinkService.MagicLinkVerificationResult result = magicLinkService.verifyMagicLink(token);
            return ResponseEntity.ok(new JwtResponse(
                result.getToken(), 
                result.isProfileCompleted(), 
                result.isNewRegistration()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error verifying magic link", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Magic link verification failed: " + e.getMessage()));
        }
    }
}
