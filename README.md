# HealthTrack - 健康管理系统

## 项目简介

HealthTrack是一个基于Spring Boot + Vue.js的健康管理系统，支持普通用户和医生两种角色，提供用户注册、登录、健康信息管理等功能。

## 技术栈

### 后端技术
- **Java**: 1.8.0_461
- **Spring Boot**: 2.7.18
- **Spring Security**: 5.7.x
- **Spring Data JPA**: 2.7.x
- **MySQL**: 8.0+
- **JWT**: 0.11.5
- **Maven**:  3.9.11

### 前端技术
- **Vue.js**: 3.x
- **Element Plus**: 2.x
- **Vue Router**: 4.x
- **Pinia**: 2.x
- **Vite**: 4.x

## 项目结构

```
Project/
├── backend/                    # Spring Boot后端
│   ├── src/main/java/com/healthtrack/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # REST控制器
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # JPA实体类
│   │   ├── repository/        # 数据访问层
│   │   ├── security/         # 安全配置
│   │   ├── service/          # 业务逻辑层
│   │   └── HealthTrackApplication.java  # 主启动类
│   ├── src/main/resources/
│   │   ├── application.yml    # 应用配置
│   │   └── sql/init.sql      # 数据库初始化脚本
│   └── pom.xml               # Maven依赖配置
├── frontend/                  # Vue.js前端
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   ├── stores/           # 状态管理
│   │   ├── router/           # 路由配置
│   │   ├── App.vue           # 根组件
│   │   └── main.js           # 入口文件
│   ├── package.json          # 依赖配置
│   └── vite.config.js        # Vite配置
└── README.md                 # 项目说明文档
```

## 核心类说明

### 后端核心类

#### 1. 实体类 (Entity)

**User.java** - 普通用户实体
- 实现`UserDetails`接口，支持Spring Security认证
- 字段：id, username, password, healthId, name, phone, createdAt, updatedAt
- 角色：ROLE_USER

**Doctor.java** - 医生实体
- 实现`UserDetails`接口，支持Spring Security认证
- 字段：id, username, password, licenseId, name, phone, specialization, verified, createdAt, updatedAt
- 角色：ROLE_DOCTOR

#### 2. 数据传输对象 (DTO)

**RegisterRequest.java** - 注册请求对象
- 字段：username, password, userType
- 支持普通用户和医生注册
- 简化设计，只包含必要字段

**LoginRequest.java** - 登录请求对象
- 字段：username, password, userType

**AuthResponse.java** - 认证响应对象
- 字段：token, type, id, username, role
- 简化设计，只包含必要字段

#### 3. 控制器 (Controller)

**AuthController.java** - 认证控制器
- `/api/auth/login` - 用户登录
- `/api/auth/register` - 用户注册
- `/api/auth/roles` - 获取可用角色

**UserController.java** - 用户控制器
- `/api/user/profile` - 获取用户信息
- `/api/user/check-username` - 检查用户名是否存在
- `/api/user/check-health-id` - 检查健康卡号是否存在
- `/api/user/check-phone` - 检查手机号是否存在

#### 4. 服务层 (Service)

**AuthService.java** - 认证服务
- `login()` - 处理用户登录，支持User和Doctor类型
- `register()` - 处理用户注册，根据userType创建不同用户
- `registerUser()` - 注册普通用户
- `registerDoctor()` - 注册医生

**UserService.java** - 用户服务
- 实现`UserDetailsService`接口
- 提供用户CRUD操作和认证功能

**DoctorService.java** - 医生服务
- 实现`UserDetailsService`接口
- 提供医生CRUD操作和认证功能

**UnifiedUserDetailsService.java** - 统一用户详情服务
- 实现`UserDetailsService`接口
- 使用ThreadLocal机制根据用户类型精确查找User或Doctor
- 支持同名但不同身份的用户区分

#### 5. 数据访问层 (Repository)

**UserRepository.java** - 用户数据访问接口
- 继承`JpaRepository<User, Long>`
- 提供用户名、健康ID、手机号查询方法

**DoctorRepository.java** - 医生数据访问接口
- 继承`JpaRepository<Doctor, Long>`
- 提供用户名、执照ID、手机号查询方法

#### 6. 安全配置 (Security)

**WebSecurityConfig.java** - Spring Security配置
- 配置CORS、CSRF、认证提供者
- 设置JWT过滤器
- 配置URL访问权限

**JwtUtils.java** - JWT工具类
- `generateJwtToken()` - 生成JWT token
- `getUserNameFromJwtToken()` - 从token中获取用户名
- `validateJwtToken()` - 验证token有效性

**AuthTokenFilter.java** - JWT认证过滤器
- 拦截请求，验证JWT token
- 设置SecurityContext

**AuthEntryPointJwt.java** - JWT认证入口点
- 处理认证失败的情况

#### 7. 配置类 (Config)

**DataInitializer.java** - 数据初始化器
- 实现`CommandLineRunner`接口
- 应用启动时的初始化逻辑（测试数据生成已移除）

### 前端核心组件

#### 1. 页面组件 (Views)

**Login.vue** - 登录页面
- 用户名/密码登录
- 表单验证
- 自动跳转到对应Dashboard

**Register.vue** - 注册页面
- 支持普通用户和医生注册
- 表单验证
- 用户名、密码、用户类型选择

**Dashboard.vue** - 普通用户仪表板
- 用户信息展示
- 健康数据管理

**DoctorDashboard.vue** - 医生仪表板
- 医生信息展示
- 患者管理功能

**Profile.vue** - 用户资料页面
- 个人信息编辑
- 密码修改

#### 2. 状态管理 (Stores)

**auth.js** - 认证状态管理
- 用户登录状态
- JWT token管理
- 登录/注册/登出方法

#### 3. 路由配置 (Router)

**index.js** - 路由配置
- 页面路由定义
- 路由守卫配置
- 根据用户角色跳转

## 数据库设计

### 用户表 (system_user)
```sql
CREATE TABLE system_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    health_id BIGINT UNIQUE,
    name VARCHAR(128),
    phone VARCHAR(20) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 医生表 (system_provider)
```sql
CREATE TABLE system_provider (
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
);
```

## 环境要求

### 开发环境
- **JDK**: 8 或更高版本
- **Maven**: 3.6 或更高版本
- **Node.js**: 16 或更高版本
- **MySQL**: 8.0 或更高版本

### 系统要求
- **操作系统**: Windows, macOS, Linux
- **内存**: 至少 4GB RAM
- **磁盘空间**: 至少 2GB 可用空间

## 安装和启动

### 1. 克隆项目
```bash
git clone <repository-url>
cd Project
```

### 2. 数据库配置

#### 安装MySQL
- 下载并安装MySQL 8.0+
- 创建数据库用户和密码

#### 配置数据库连接
编辑 `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthtrack?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 12345678  # 修改为你的MySQL密码
```

#### 初始化数据库
```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source backend/src/main/resources/sql/init.sql
```

### 3. 启动后端服务

#### 方式一：使用Maven命令
```bash
cd backend
mvn clean package
mvn spring-boot:run
```

#### 方式二：使用IDE
- 导入Maven项目到IDE
- 运行 `HealthTrackApplication.java`

#### 方式三：使用JAR文件
```bash
cd backend
mvn clean package -DskipTests
java -jar target/healthtrack-backend-1.0.0.jar
```

后端服务将在 `http://localhost:8001` 启动

### 4. 启动前端服务

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:3001` 启动

## 系统特性

### 同名用户身份区分
系统支持同名但不同身份的用户：
- 同一个用户名可以同时存在普通用户和医生两种身份
- 登录时根据前端选择的用户类型精确识别身份
- 使用ThreadLocal机制确保身份识别的准确性

### 简化的注册流程
- 注册时只需提供：用户名、密码、用户类型
- 其他信息（姓名、健康卡号、手机号等）为可选字段
- 支持后续完善个人信息

## 登录注册功能实现逻辑

### 注册功能实现

#### 1. 前端注册流程
```javascript
// Register.vue
const handleRegister = async () => {
  // 1. 表单验证
  await registerFormRef.value.validate()
  
  // 2. 准备注册数据
  const registerData = {
    username: registerForm.username,
    password: registerForm.password,
    userType: registerForm.userType  // USER 或 DOCTOR
  }
  
  // 3. 调用注册API
  const response = await authStore.register(registerData)
  
  // 4. 根据前端选择的用户类型跳转
  if (registerForm.userType === 'DOCTOR') {
    router.push('/doctor-dashboard')
  } else {
    router.push('/dashboard')
  }
}
```

#### 2. 后端注册处理
```java
// AuthService.java
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
    // 1. 检查用户名是否已存在
    if (userService.existsByUsername(registerRequest.getUsername())) {
        throw new RuntimeException("Username is already taken!");
    }
    
    // 2. 创建用户（只使用用户名和密码）
    User user = new User(registerRequest.getUsername(), registerRequest.getPassword());
    
    // 3. 保存到数据库
    User savedUser = userService.createUser(user);
    
    // 4. 自动登录并返回JWT token
    return login(new LoginRequest(registerRequest.getUsername(), 
                                 registerRequest.getPassword(), 
                                 registerRequest.getUserType()));
}
```

### 登录功能实现

#### 1. 前端登录流程
```javascript
// Login.vue
const handleLogin = async () => {
  // 1. 表单验证
  await loginFormRef.value.validate()
  
  // 2. 调用登录API
  await authStore.login(loginForm)
  
  // 3. 根据前端选择的用户类型跳转
  if (loginForm.userType === 'DOCTOR') {
    router.push('/doctor-dashboard')
  } else {
    router.push('/dashboard')
  }
}
```

#### 2. 后端登录处理
```java
// AuthService.java
public AuthResponse login(LoginRequest loginRequest) {
    try {
        // 1. 设置当前线程的用户类型（关键步骤）
        UnifiedUserDetailsService.setCurrentUserType(loginRequest.getUserType());
        
        // 2. Spring Security认证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        // 3. 生成JWT token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 4. 根据用户类型返回响应
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            return new AuthResponse(jwt, user.getId(), user.getUsername(), "USER");
        } else if (userDetails instanceof Doctor) {
            Doctor doctor = (Doctor) userDetails;
            return new AuthResponse(jwt, doctor.getId(), doctor.getUsername(), "DOCTOR");
        }
    } finally {
        // 5. 清理ThreadLocal，避免内存泄漏
        UnifiedUserDetailsService.clearCurrentUserType();
    }
}
```

### 身份识别机制

#### ThreadLocal机制实现
```java
// UnifiedUserDetailsService.java
public class UnifiedUserDetailsService implements UserDetailsService {
    
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
            // 只在医生表中查找
            return doctorService.loadUserByUsername(username);
        } else {
            // 只在用户表中查找
            return userService.loadUserByUsername(username);
        }
    }
}
```

### 数据流转过程

#### 注册数据流
```
前端Register.vue 
    ↓ (POST /api/auth/register)
AuthController.registerUser()
    ↓
AuthService.register()
    ↓ (根据userType)
AuthService.registerUser() 或 registerDoctor()
    ↓
UserService.createUser() 或 DoctorService.createDoctor()
    ↓
UserRepository.save() 或 DoctorRepository.save()
    ↓
数据库存储
    ↓
自动登录返回JWT token
    ↓
前端跳转到对应Dashboard
```

#### 登录数据流
```
前端Login.vue
    ↓ (POST /api/auth/login)
AuthController.authenticateUser()
    ↓
AuthService.login()
    ↓ (设置ThreadLocal)
UnifiedUserDetailsService.setCurrentUserType()
    ↓
Spring Security认证
    ↓ (根据ThreadLocal中的userType)
UnifiedUserDetailsService.loadUserByUsername()
    ↓ (精确查找)
UserService 或 DoctorService
    ↓
返回UserDetails
    ↓
生成JWT token
    ↓
返回AuthResponse
    ↓
前端跳转到对应Dashboard
```

### 关键技术点

#### 1. **同名用户区分**
- 使用ThreadLocal存储用户类型
- 在认证前设置用户类型
- 根据用户类型精确查找对应表

#### 2. **JWT Token管理**
- 使用HS512算法，密钥长度512位以上
- Token包含用户ID、用户名等信息
- 前端自动在请求头中添加Authorization

#### 3. **前端状态管理**
- 使用Pinia管理认证状态
- 自动保存token到localStorage
- 根据用户类型决定页面跳转

#### 4. **安全机制**
- Spring Security提供认证框架
- BCrypt密码加密
- CORS跨域配置
- JWT token过期机制

## 使用说明

### 1. 访问应用
打开浏览器访问 `http://localhost:5173`

### 2. 注册用户
- 点击"注册"按钮
- 填写用户名、密码
- 选择用户类型（普通用户/医生）
- 点击"注册"

### 3. 登录系统
- 输入用户名和密码
- 点击"登录"
- 系统会根据用户类型跳转到对应Dashboard

### 4. 创建测试账户
系统不会自动创建测试账户，您需要手动注册：
1. 访问注册页面
2. 填写用户名和密码
3. 选择用户类型（普通用户/医生）
4. 完成注册

## API接口文档

### 认证接口

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "userType": "USER"
}
```

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "userType": "USER"
}
```

#### 登录/注册响应
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "newuser",
  "role": "USER"
}
```

### 用户接口

#### 获取用户信息
```http
GET /api/user/profile
Authorization: Bearer <jwt-token>
```

#### 检查用户名是否存在
```http
GET /api/user/check-username?username=testuser
```

## 配置说明

### JWT配置
```yaml
jwt:
  secret: mySuperSecureKey_ThisKeyMustBeAtLeast64BytesLong_UseItForHS512Algorithm!!!
  expiration: 86400000  # 24小时
```

### 日志配置
```yaml
logging:
  level:
    com.healthtrack: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

## 常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 验证数据库连接配置
- 确认数据库用户权限

### 2. JWT密钥错误
- 确保JWT密钥长度至少64字符
- 检查密钥配置是否正确

### 3. 前端跨域问题
- 后端已配置CORS，允许所有来源
- 检查前端请求URL是否正确

### 4. 用户认证失败
- 检查用户名和密码是否正确
- 确认用户类型选择正确
- 查看后端日志获取详细错误信息

## 开发指南

### 添加新功能
1. 在`entity`包中创建实体类
2. 在`repository`包中创建数据访问接口
3. 在`service`包中实现业务逻辑
4. 在`controller`包中创建REST接口
5. 在前端`views`包中创建页面组件

### 数据库迁移
- 修改实体类后，Hibernate会自动更新表结构
- 生产环境建议使用Flyway或Liquibase进行数据库版本管理

### 安全注意事项
- JWT密钥应使用环境变量配置
- 生产环境应使用HTTPS
- 定期更新依赖版本

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

