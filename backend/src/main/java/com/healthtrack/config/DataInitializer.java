package com.healthtrack.config;

import com.healthtrack.entity.User;
import com.healthtrack.entity.Doctor;
import com.healthtrack.service.UserService;
import com.healthtrack.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Override
    public void run(String... args) throws Exception {
        // 创建测试用户
        if (!userService.existsByUsername("testuser")) {
            User testUser = new User("testuser", "password123");
            testUser.setName("测试用户");
            testUser.setHealthId(123456789L);
            testUser.setPhone("13800138000");
            userService.createUser(testUser);
            System.out.println("测试用户创建成功: testuser / password123");
        }
        
        // 创建测试医生
        if (!doctorService.existsByUsername("testdoctor")) {
            Doctor testDoctor = new Doctor("testdoctor", "password123");
            testDoctor.setLicenseId(123456789L);
            testDoctor.setName("测试医生");
            testDoctor.setPhone("13900139000");
            testDoctor.setSpecialization(1); // 1=内科
            doctorService.createDoctor(testDoctor);
            System.out.println("测试医生创建成功: testdoctor / password123");
        }
    }
}