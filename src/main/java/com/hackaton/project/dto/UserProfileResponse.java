package com.hackaton.project.dto;

import com.hackaton.project.model.User;

public class UserProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private boolean profileCompleted;
    
    // Constructors
    public UserProfileResponse() {
    }
    
    public UserProfileResponse(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.profileCompleted = user.isProfileCompleted();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public Integer getAge() {
        return age;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
}
