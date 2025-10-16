#!/bin/bash

echo "=========================================="
echo "HealthTrack 前端启动脚本"
echo "=========================================="

# 检查Node.js版本
echo "检查Node.js版本..."
node -version

# 检查npm版本
echo "检查npm版本..."
npm -version

echo ""
echo "启动前端服务..."
echo "前端服务将在 http://localhost:3000 启动"
echo "请确保后端服务已启动 (http://localhost:8080)"
echo ""
echo "按 Ctrl+C 停止服务"
echo "=========================================="

# 进入前端目录
cd frontend

# 检查是否存在node_modules
if [ ! -d "node_modules" ]; then
    echo "安装项目依赖..."
    npm install
else
    echo "依赖已安装，跳过npm install"
fi

# 启动开发服务器
npm run dev
