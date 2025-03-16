package com.hackaton.project.dto;

import java.util.Objects;

/**
 * Response DTO for JWT authentication containing token and user status information.
 */
public final class JwtResponse {
    private final String token;
    private final boolean profileCompleted;
    private final boolean newRegistration;
    
    public JwtResponse(String token, boolean profileCompleted, boolean newRegistration) {
        this.token = Objects.requireNonNull(token, "Token cannot be null");
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return profileCompleted == that.profileCompleted && 
               newRegistration == that.newRegistration && 
               Objects.equals(token, that.token);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(token, profileCompleted, newRegistration);
    }
} 