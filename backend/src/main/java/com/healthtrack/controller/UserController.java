package com.healthtrack.controller;

import com.healthtrack.entity.User;
import com.healthtrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR')")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("name", user.getName());
        profile.put("username", user.getUsername());
        profile.put("phone", user.getPhone());
        profile.put("healthId", user.getHealthId());
        profile.put("role", "USER");
        profile.put("createdAt", user.getCreatedAt());
        
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsernameExists(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-health-id")
    public ResponseEntity<?> checkHealthIdExists(@RequestParam Long healthId) {
        boolean exists = userService.existsByHealthId(healthId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-phone")
    public ResponseEntity<?> checkPhoneExists(@RequestParam String phone) {
        boolean exists = userService.existsByPhone(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}