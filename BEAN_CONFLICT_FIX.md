# Bean定义冲突修复总结

## 问题描述

启动Spring Boot应用时出现bean定义冲突错误：

```
The bean 'passwordEncoder', defined in class path resource [com/healthtrack/security/WebSecurityConfig.class], could not be registered. A bean with that name has already been defined in class path resource [com/healthtrack/config/SecurityConfig.class] and overriding is disabled.
```

## 问题原因

项目中存在两个配置类都定义了相同名称的`passwordEncoder` bean：

1. **SecurityConfig.java** (第12行)：
   ```java
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   ```

2. **WebSecurityConfig.java** (第58行)：
   ```java
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   ```

## 修复方案

删除了重复的`SecurityConfig.java`文件，保留`WebSecurityConfig.java`中的bean定义。

### 删除的文件
- `/backend/src/main/java/com/healthtrack/config/SecurityConfig.java`

### 保留的配置
- `/backend/src/main/java/com/healthtrack/security/WebSecurityConfig.java` 中的 `passwordEncoder` bean

## 技术说明

### Bean定义冲突
当Spring容器启动时，如果发现两个或多个配置类定义了相同名称的bean，会抛出`BeanDefinitionOverrideException`异常。

### 解决方案选择
1. **删除重复配置**：删除其中一个bean定义（推荐）
2. **重命名bean**：给其中一个bean起不同的名字
3. **启用覆盖**：设置`spring.main.allow-bean-definition-overriding=true`（不推荐）

### 为什么选择删除SecurityConfig
1. **功能重复**：两个类都定义了相同的`passwordEncoder` bean
2. **职责清晰**：`WebSecurityConfig`是主要的Security配置类
3. **避免混淆**：减少重复的配置类

## 验证修复

修复后应该能够：
1. ✅ 正常启动Spring Boot应用
2. ✅ 正确注入`passwordEncoder` bean
3. ✅ 正常处理密码加密功能

## 测试建议

1. **启动测试**：
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **功能测试**：
   - 尝试注册新用户，验证密码加密功能
   - 尝试登录，验证密码验证功能

## 注意事项

1. **配置集中**：将Security相关的配置集中在`WebSecurityConfig`中
2. **避免重复**：确保每个bean只在一个地方定义
3. **命名规范**：使用清晰的bean名称避免冲突

## 相关文件

- ✅ **保留**：`WebSecurityConfig.java` - 主要的Security配置类
- ❌ **删除**：`SecurityConfig.java` - 重复的配置类

现在应用应该可以正常启动了！
