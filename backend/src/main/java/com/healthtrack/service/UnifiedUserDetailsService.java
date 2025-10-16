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
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 首先尝试在普通用户中查找
        try {
            return userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // 如果普通用户中没找到，再在医生中查找
            try {
                return doctorService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }
    }
}
