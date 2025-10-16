package com.healthtrack.service;

import com.healthtrack.dto.AuthResponse;
import com.healthtrack.dto.LoginRequest;
import com.healthtrack.dto.RegisterRequest;
import com.healthtrack.entity.Doctor;
import com.healthtrack.entity.User;
import com.healthtrack.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // 根据用户类型创建不同的响应
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            return new AuthResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getName(),
                "USER",
                user.getHealthId()
            );
        } else if (userDetails instanceof Doctor) {
            Doctor doctor = (Doctor) userDetails;
            return new AuthResponse(
                jwt,
                doctor.getId(),
                doctor.getUsername(),
                doctor.getName(),
                "DOCTOR",
                doctor.getLicenseId() // 医生使用licenseId作为healthId
            );
        } else {
            throw new RuntimeException("Unknown user type");
        }
    }
    
    public AuthResponse register(RegisterRequest registerRequest) {
        String userType = registerRequest.getUserType();
        
        if ("USER".equals(userType)) {
            return registerUser(registerRequest);
        } else if ("DOCTOR".equals(userType)) {
            return registerDoctor(registerRequest);
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
    
    private AuthResponse registerUser(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        // 创建用户（简化版本，只使用用户名和密码）
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        // 可选字段设置为null或默认值
        user.setName(registerRequest.getName());
        user.setHealthId(registerRequest.getHealthId());
        user.setPhone(registerRequest.getPhone());
        
        User savedUser = userService.createUser(user);
        
        // 自动登录
        return login(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getUserType()));
    }
    
    private AuthResponse registerDoctor(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (doctorService.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        // 创建医生（简化版本，只使用用户名和密码）
        Doctor doctor = new Doctor();
        doctor.setUsername(registerRequest.getUsername());
        doctor.setPassword(registerRequest.getPassword());
        // 可选字段设置为null或默认值
        doctor.setLicenseId(registerRequest.getLicenseId());
        doctor.setName(registerRequest.getName());
        doctor.setPhone(registerRequest.getPhone());
        doctor.setSpecialization(registerRequest.getSpecialization());
        
        Doctor savedDoctor = doctorService.createDoctor(doctor);
        
        // 自动登录
        return login(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getUserType()));
    }
}