# MySQL数据库配置指南

## 1. 项目文件修改总结

### 1.1 pom.xml修改
添加了MySQL驱动依赖：
```xml
<!-- MySQL Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 1.2 application.yml修改
- **数据库连接**：从H2改为MySQL
- **端口**：从8001改为8080
- **DDL模式**：从create-drop改为update
- **方言**：从H2Dialect改为MySQL8Dialect
- **日志级别**：添加了SQL调试日志

### 1.3 新增文件
- `src/main/resources/sql/init.sql` - 数据库初始化脚本

## 2. MySQL配置注意事项

### 2.1 数据库准备

#### 安装MySQL
```bash
# macOS (使用Homebrew)
brew install mysql
brew services start mysql

# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql

# CentOS/RHEL
sudo yum install mysql-server
sudo systemctl start mysqld
```

#### 创建数据库和用户
```sql
-- 登录MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE healthtrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选，推荐）
CREATE USER 'healthtrack'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON healthtrack.* TO 'healthtrack'@'localhost';
FLUSH PRIVILEGES;
```

### 2.2 配置文件修改

#### 修改application.yml中的数据库连接信息
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthtrack?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root  # 或你创建的用户名
    password: your_password_here  # 修改为你的密码
```

#### 重要配置说明
- `useSSL=false` - 禁用SSL（开发环境）
- `serverTimezone=UTC` - 设置时区
- `allowPublicKeyRetrieval=true` - 允许公钥检索
- `ddl-auto: update` - 自动更新表结构
- `MySQL8Dialect` - 使用MySQL8方言

### 2.3 安全注意事项

#### 生产环境配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthtrack?useSSL=true&serverTimezone=UTC
    username: healthtrack_user
    password: ${DB_PASSWORD}  # 使用环境变量
```

#### 环境变量设置
```bash
# 设置数据库密码
export DB_PASSWORD=your_secure_password

# 或在application-prod.yml中配置
spring:
  datasource:
    password: ${DB_PASSWORD:default_password}
```

### 2.4 数据库连接池配置（可选）

#### 添加HikariCP配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

## 3. 运行步骤

### 3.1 启动MySQL服务
```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
# 或
sudo service mysql start
```

### 3.2 执行初始化脚本
```bash
# 登录MySQL并执行脚本
mysql -u root -p < backend/src/main/resources/sql/init.sql
```

### 3.3 启动应用
```bash
cd backend
mvn spring-boot:run
```

## 4. 常见问题解决

### 4.1 连接问题
```
Error: Access denied for user 'root'@'localhost'
```
**解决方案**：
```sql
-- 重置root密码
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### 4.2 时区问题
```
Error: The server time zone value 'CST' is unrecognized
```
**解决方案**：
```yaml
# 在URL中添加时区参数
url: jdbc:mysql://localhost:3306/healthtrack?serverTimezone=Asia/Shanghai
```

### 4.3 SSL问题
```
Error: SSL connection error
```
**解决方案**：
```yaml
# 开发环境禁用SSL
url: jdbc:mysql://localhost:3306/healthtrack?useSSL=false
```

### 4.4 字符编码问题
```
Error: Incorrect string value
```
**解决方案**：
```sql
-- 确保数据库使用utf8mb4
ALTER DATABASE healthtrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 5. 性能优化建议

### 5.1 索引优化
```sql
-- 为常用查询字段添加索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_health_id ON users(health_id);
CREATE INDEX idx_doctors_license_id ON doctors(license_id);
```

### 5.2 连接池优化
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

### 5.3 JPA优化
```yaml
spring:
  jpa:
    open-in-view: false  # 避免懒加载问题
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
```

## 6. 监控和维护

### 6.1 数据库监控
```sql
-- 查看连接状态
SHOW PROCESSLIST;

-- 查看表状态
SHOW TABLE STATUS;

-- 查看索引使用情况
SHOW INDEX FROM users;
```

### 6.2 备份策略
```bash
# 备份数据库
mysqldump -u root -p healthtrack > healthtrack_backup.sql

# 恢复数据库
mysql -u root -p healthtrack < healthtrack_backup.sql
```

## 7. 测试验证

### 7.1 连接测试
```bash
# 测试数据库连接
mysql -u root -p -e "SELECT 1;"
```

### 7.2 应用测试
```bash
# 启动应用
cd backend
mvn spring-boot:run

# 测试API
curl http://localhost:8080/api/auth/roles
```

现在你的项目已经配置为使用MySQL数据库了！
