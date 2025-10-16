#!/bin/bash

echo "=========================================="
echo "HealthTrack 项目启动脚本"
echo "=========================================="

# 检查Java版本
echo "检查Java版本..."
java -version

echo ""
echo "启动后端服务..."
echo "后端服务将在 http://localhost:8080 启动"
echo "H2数据库控制台: http://localhost:8080/h2-console"
echo ""
echo "测试账户:"
echo "用户: testuser / password123"
echo "医生: testdoctor / password123"
echo ""
echo "按 Ctrl+C 停止服务"
echo "=========================================="

# 进入后端目录并启动
cd backend
mvn spring-boot:run
