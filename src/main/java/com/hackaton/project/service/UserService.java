package com.hackaton.project.service;

import com.hackaton.project.dto.UserProfileRequest;
import com.hackaton.project.dto.UserProfileResponse;
import com.hackaton.project.model.User;
import com.hackaton.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional
    public UserProfileResponse updateUserProfile(String email, UserProfileRequest profileRequest) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        user.setFirstName(profileRequest.getFirstName());
        user.setLastName(profileRequest.getLastName());
        user.setAge(profileRequest.getAge());
        user.setProfileCompleted(true);
        
        User updatedUser = userRepository.save(user);
        return new UserProfileResponse(updatedUser);
    }
    
    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return new UserProfileResponse(user);
    }
}
