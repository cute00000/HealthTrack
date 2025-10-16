# MySQL连接问题解决方案

## 问题分析

错误信息：`Access denied for user 'root'@'localhost' (using password: YES)`

这表明：
1. MySQL服务正在运行
2. 用户名正确（root）
3. 密码不正确或用户权限不足

## 解决步骤

### 1. 检查MySQL服务状态

```bash
# macOS
brew services list | grep mysql
brew services start mysql

# Linux
sudo systemctl status mysql
sudo systemctl start mysql
```

### 2. 测试MySQL连接

```bash
# 尝试连接MySQL
mysql -u root -p
# 输入你的MySQL root密码
```

### 3. 如果连接失败，重置root密码

#### 方法1：使用mysqladmin
```bash
# 停止MySQL服务
brew services stop mysql
# 或 sudo systemctl stop mysql

# 以安全模式启动MySQL
mysqld_safe --skip-grant-tables &

# 连接MySQL（无需密码）
mysql -u root

# 重置密码
USE mysql;
UPDATE user SET authentication_string=PASSWORD('new_password') WHERE User='root';
FLUSH PRIVILEGES;
EXIT;

# 重启MySQL服务
brew services restart mysql
```

#### 方法2：使用ALTER USER（如果能够连接）
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### 4. 创建数据库和用户

```sql
-- 登录MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE IF NOT EXISTS healthtrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建专用用户（推荐）
CREATE USER 'healthtrack'@'localhost' IDENTIFIED BY 'healthtrack123';
GRANT ALL PRIVILEGES ON healthtrack.* TO 'healthtrack'@'localhost';
FLUSH PRIVILEGES;

-- 验证用户创建
SELECT User, Host FROM mysql.user WHERE User='healthtrack';
```

### 5. 修改application.yml配置

根据你的MySQL设置，选择以下配置之一：

#### 选项1：使用root用户
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthtrack?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_actual_root_password  # 修改为你的实际密码
```

#### 选项2：使用专用用户（推荐）
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthtrack?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: healthtrack
    password: healthtrack123
```

### 6. 执行数据库初始化脚本

```bash
# 执行初始化脚本
mysql -u root -p healthtrack < backend/src/main/resources/sql/init.sql
# 或
mysql -u healthtrack -p healthtrack < backend/src/main/resources/sql/init.sql
```

### 7. 验证配置

```bash
# 测试数据库连接
mysql -u root -p -e "USE healthtrack; SHOW TABLES;"
# 或
mysql -u healthtrack -p -e "USE healthtrack; SHOW TABLES;"
```

## 常见问题解决

### 问题1：MySQL服务未启动
```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
```

### 问题2：端口被占用
```bash
# 检查3306端口
lsof -i :3306
# 或
netstat -an | grep 3306
```

### 问题3：权限问题
```sql
-- 检查用户权限
SHOW GRANTS FOR 'root'@'localhost';
SHOW GRANTS FOR 'healthtrack'@'localhost';
```

### 问题4：字符编码问题
```sql
-- 检查数据库字符集
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME 
FROM information_schema.SCHEMATA 
WHERE SCHEMA_NAME = 'healthtrack';
```

## 测试步骤

1. **启动MySQL服务**
2. **测试连接**：`mysql -u root -p`
3. **创建数据库**：执行SQL脚本
4. **修改配置**：更新application.yml中的密码
5. **启动应用**：`mvn spring-boot:run`

## 安全建议

1. **不要使用root用户**：创建专用数据库用户
2. **使用强密码**：避免使用简单密码
3. **限制权限**：只授予必要的数据库权限
4. **生产环境**：使用环境变量存储密码

现在按照这些步骤操作，应该可以解决MySQL连接问题！
