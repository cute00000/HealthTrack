# HealthTrack 快速启动指南

## 环境要求
- Java 17+
- Node.js 16+
- Maven 3.6+

## 快速启动

### 1. 启动后端服务
```bash
# 方法1: 使用脚本
chmod +x start-backend.sh
./start-backend.sh

# 方法2: 手动启动
cd backend
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 2. 启动前端服务
```bash
# 方法1: 使用脚本
chmod +x start-frontend.sh
./start-frontend.sh

# 方法2: 手动启动
cd frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

### 3. 访问应用
打开浏览器访问 `http://localhost:3000`

## 测试账户

### 用户账户
- 用户名: `testuser`
- 密码: `password123`
- 角色: 用户

### 医生账户
- 用户名: `testdoctor`
- 密码: `password123`
- 角色: 医生

## 功能说明

### 登录功能
- 支持用户名+密码登录
- 支持用户和医生两种角色
- JWT令牌认证

### 注册功能
- 用户注册：需要填写姓名、用户名、健康ID、手机号、密码
- 医生注册：额外需要填写医疗执照号、专业领域
- 自动验证用户名、健康ID、手机号唯一性

### 个人资料
- 查看个人信息
- 支持用户和医生两种角色显示
- 医生可查看医疗执照和专业领域信息

## 常见问题

### Q: 后端启动失败？
A: 检查Java版本是否为17+，Maven是否正确安装

### Q: 前端启动失败？
A: 检查Node.js版本是否为16+，运行 `npm install` 安装依赖

### Q: 登录失败？
A: 使用提供的测试账户，或注册新账户

### Q: 数据库访问？
A: 访问 `http://localhost:8080/h2-console` 查看H2数据库
- JDBC URL: `jdbc:h2:mem:healthtrack`
- 用户名: `sa`
- 密码: `password`
