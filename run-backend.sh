#!/bin/bash

echo "启动HealthTrack后端服务..."
echo "================================"

# 检查Java版本
echo "检查Java版本..."
java -version

# 进入后端目录
cd backend

# 检查Maven是否安装
echo "检查Maven..."
mvn -version

# 清理并编译项目
echo "清理并编译项目..."
mvn clean compile

# 启动应用
echo "启动SpringBoot应用..."
echo "后端服务将在 http://localhost:8080 启动"
echo "H2数据库控制台: http://localhost:8080/h2-console"
echo ""
echo "测试账户:"
echo "用户: user@test.com / password123"
echo "医生: doctor@test.com / password123"
echo ""
echo "按 Ctrl+C 停止服务"
echo "================================"

mvn spring-boot:run
