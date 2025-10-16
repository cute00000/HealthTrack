package com.healthtrack.service;

import com.healthtrack.entity.Doctor;
import com.healthtrack.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UnifiedUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    // 存储当前登录请求的用户类型
    private static final ThreadLocal<String> currentUserType = new ThreadLocal<>();
    
    public static void setCurrentUserType(String userType) {
        currentUserType.set(userType);
    }
    
    public static void clearCurrentUserType() {
        currentUserType.remove();
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userType = currentUserType.get();
        
        if ("DOCTOR".equals(userType)) {
            // 如果指定了医生类型，只查找医生
            try {
                return doctorService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("Doctor not found with username: " + username);
            }
        } else {
            // 默认查找普通用户
            try {
                return userService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }
    }
}
