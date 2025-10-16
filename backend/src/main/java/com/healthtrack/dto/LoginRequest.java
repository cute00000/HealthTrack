package com.healthtrack.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    private String userType;
    
    // Constructors
    public LoginRequest() {}
    
    public LoginRequest(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
}