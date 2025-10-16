-- HealthTrack数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS healthtrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE healthtrack;

-- 创建用户表
CREATE TABLE IF NOT EXISTS system_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    health_id BIGINT UNIQUE,
    name VARCHAR(128),
    phone VARCHAR(20) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建医生表
CREATE TABLE IF NOT EXISTS system_provider (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    license_id BIGINT UNIQUE,
    name VARCHAR(128),
    phone VARCHAR(20) UNIQUE,
    role TINYINT,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ;

-- 插入测试数据
INSERT INTO users (username, password, health_id, name, phone) VALUES 
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '123456789', '测试用户', '13800138000')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO doctors (username, password, license_id, name, phone, specialization) VALUES 
('testdoctor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ML123456789', '测试医生', '13900139000', '内科')
ON DUPLICATE KEY UPDATE username=username;

-- 显示表结构
DESCRIBE users;
DESCRIBE doctors;

-- 显示测试数据
SELECT 'Users:' as table_name;
SELECT * FROM users;
SELECT 'Doctors:' as table_name;
SELECT * FROM doctors;
