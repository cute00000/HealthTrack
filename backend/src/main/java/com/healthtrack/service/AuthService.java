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
        try {
            // 设置当前线程的用户类型，用于UnifiedUserDetailsService判断
            UnifiedUserDetailsService.setCurrentUserType(loginRequest.getUserType());
            
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
                    "USER"
                );
            } else if (userDetails instanceof Doctor) {
                Doctor doctor = (Doctor) userDetails;
                return new AuthResponse(
                    jwt,
                    doctor.getId(),
                    doctor.getUsername(),
                    "DOCTOR"
                );
            } else {
                throw new RuntimeException("Unknown user type");
            }
        } finally {
            // 清理ThreadLocal，避免内存泄漏
            UnifiedUserDetailsService.clearCurrentUserType();
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
        User user = new User(registerRequest.getUsername(), registerRequest.getPassword());
        
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
        Doctor doctor = new Doctor(registerRequest.getUsername(), registerRequest.getPassword());
        
        Doctor savedDoctor = doctorService.createDoctor(doctor);
        
        // 自动登录
        return login(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getUserType()));
    }
}