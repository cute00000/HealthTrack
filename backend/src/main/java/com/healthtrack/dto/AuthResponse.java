package com.healthtrack.dto;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String name;
    private String role;
    private Long healthId;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String token, Long id, String username, String name, String role, Long healthId) {
        this.token = token;
        this.id = id;
        this.email = username; // 使用username作为email字段
        this.name = name;
        this.role = role;
        this.healthId = healthId;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Long getHealthId() {
        return healthId;
    }
    
    public void setHealthId(Long healthId) {
        this.healthId = healthId;
    }
}
