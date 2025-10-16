package com.healthtrack.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        // 数据初始化逻辑已移除
        // 如需测试数据，请手动创建或使用数据库脚本
        System.out.println("HealthTrack应用启动完成");
    }
}