package com.healthtrack.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {
    
    @Size(min = 2, max = 50)
    private String name;
    
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    
    private Long healthId;
    
    private String phone;
    
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    
    private String userType;
    
    // For doctor registration
    private Long licenseId;
    private Integer specialization;
    
    // Constructors
    public RegisterRequest() {}
    
    public RegisterRequest(String name, String username, Long healthId, String phone, String password, String userType) {
        this.name = name;
        this.username = username;
        this.healthId = healthId;
        this.phone = phone;
        this.password = password;
        this.userType = userType;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getHealthId() {
        return healthId;
    }
    
    public void setHealthId(Long healthId) {
        this.healthId = healthId;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
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
    
    public Long getLicenseId() {
        return licenseId;
    }
    
    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }
    
    public Integer getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(Integer specialization) {
        this.specialization = specialization;
    }
}