package com.healthtrack.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Collection;

@Entity
@Table(name = "system_provider")
public class Doctor implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "license_id", unique = true)
    private Long licenseId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "phone", unique = true)
    private String phone;
    
    @Column(name = "role")
    private Integer specialization; // 医生的类别，使用数字编码
    
    @Column(name = "is_verified")
    private Boolean verified = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    // Constructors
    public Doctor() {}
    
    public Doctor(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Doctor(String username, String password, Long licenseId, String name, String phone, Integer specialization) {
        this.username = username;
        this.password = password;
        this.licenseId = licenseId;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Long getLicenseId() {
        return licenseId;
    }
    
    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Integer getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(Integer specialization) {
        this.specialization = specialization;
    }
    
    public Boolean getVerified() {
        return verified;
    }
    
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
