# MySQL依赖版本号修复

## 问题描述

Maven编译时出现错误：
```
'dependencies.dependency.version' for mysql:mysql-connector-java:jar is missing. @ line 55, column 21
```

## 问题原因

MySQL依赖缺少版本号。在Maven中，如果依赖没有在Spring Boot的依赖管理中定义版本，需要手动指定版本号。

## 修复方案

### 修复前
```xml
<!-- MySQL Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 修复后
```xml
<!-- MySQL Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
    <scope>runtime</scope>
</dependency>
```

## 版本选择说明

### MySQL Connector/J版本
- **8.0.33** - 当前稳定版本，兼容MySQL 8.0+
- **8.0.32** - 之前的稳定版本
- **5.1.49** - 旧版本，兼容MySQL 5.7

### Spring Boot 2.7.18兼容性
Spring Boot 2.7.18默认不管理MySQL Connector/J的版本，需要手动指定。

## 验证修复

现在可以正常编译：
```bash
mvn clean compile
```

## 其他可选方案

### 方案1：使用Spring Boot管理的版本
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```
然后在`<properties>`中添加：
```xml
<properties>
    <mysql.version>8.0.33</mysql.version>
</properties>
```

### 方案2：使用新版本的MySQL Connector
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
    <scope>runtime</scope>
</dependency>
```

## 注意事项

1. **版本兼容性**：确保MySQL Connector版本与MySQL服务器版本兼容
2. **Spring Boot版本**：不同Spring Boot版本对MySQL Connector的支持不同
3. **运行时依赖**：使用`<scope>runtime</scope>`确保只在运行时需要

## 相关文档

- [MySQL Connector/J官方文档](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [Spring Boot数据库配置](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.datasource)

现在Maven编译应该可以正常进行了！
