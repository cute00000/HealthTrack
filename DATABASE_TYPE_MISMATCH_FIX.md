# 数据库字段类型不匹配修复总结

## 问题分析

**错误信息**：
```
Incorrect integer value: 'ML123456789' for column 'license_id' at row 1
```

**问题原因**：
- 数据库表`system_provider`中`license_id`字段定义为`BIGINT`（整数类型）
- Java实体类中`licenseId`字段定义为`String`类型
- 尝试插入字符串`'ML123456789'`到整数字段导致类型不匹配

## 修复方案

### 1. 更新Doctor实体类

#### 1.1 字段类型修改
```java
// 修复前
@Column(name = "license_id", unique = true, nullable = false)
private String licenseId;

@Column(name = "role", nullable = false)
private String specialization;

// 修复后
@Column(name = "license_id", unique = true, nullable = false)
private Long licenseId;

@Column(name = "role")
private Integer specialization;
```

#### 1.2 构造器更新
```java
// 修复前
public Doctor(String username, String password, String licenseId, String name, String phone, String specialization)

// 修复后
public Doctor(String username, String password, Long licenseId, String name, String phone, Integer specialization)
```

#### 1.3 Getter/Setter方法更新
```java
// 修复前
public String getLicenseId() { return licenseId; }
public void setLicenseId(String licenseId) { this.licenseId = licenseId; }
public String getSpecialization() { return specialization; }
public void setSpecialization(String specialization) { this.specialization = specialization; }

// 修复后
public Long getLicenseId() { return licenseId; }
public void setLicenseId(Long licenseId) { this.licenseId = licenseId; }
public Integer getSpecialization() { return specialization; }
public void setSpecialization(Integer specialization) { this.specialization = specialization; }
```

### 2. 更新RegisterRequest DTO

#### 2.1 字段类型修改
```java
// 修复前
private String licenseId;
private String specialization;

// 修复后
private Long licenseId;
private Integer specialization;
```

#### 2.2 Getter/Setter方法更新
```java
// 修复前
public String getLicenseId() { return licenseId; }
public void setLicenseId(String licenseId) { this.licenseId = licenseId; }
public String getSpecialization() { return specialization; }
public void setSpecialization(String specialization) { this.specialization = specialization; }

// 修复后
public Long getLicenseId() { return licenseId; }
public void setLicenseId(Long licenseId) { this.licenseId = licenseId; }
public Integer getSpecialization() { return specialization; }
public void setSpecialization(Integer specialization) { this.specialization = specialization; }
```

### 3. 更新Repository接口

#### 3.1 DoctorRepository方法签名更新
```java
// 修复前
boolean existsByLicenseId(String licenseId);

// 修复后
boolean existsByLicenseId(Long licenseId);
```

### 4. 更新Service层

#### 4.1 DoctorService方法签名更新
```java
// 修复前
public boolean existsByLicenseId(String licenseId) {
    return doctorRepository.existsByLicenseId(licenseId);
}

// 修复后
public boolean existsByLicenseId(Long licenseId) {
    return doctorRepository.existsByLicenseId(licenseId);
}
```

### 5. 更新DataInitializer

#### 5.1 测试数据创建更新
```java
// 修复前
Doctor testDoctor = new Doctor(
    "testdoctor",
    "password123",
    "ML123456789",  // String类型
    "测试医生",
    "13900139000",
    "内科"          // String类型
);

// 修复后
Doctor testDoctor = new Doctor(
    "testdoctor",
    "password123",
    123456789L,     // Long类型
    "测试医生",
    "13900139000",
    1               // Integer类型 (1=内科)
);
```

## 数据库字段映射

### system_provider表结构
```sql
CREATE TABLE system_provider (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    license_id BIGINT UNIQUE,        -- 整数类型
    name VARCHAR(128),
    phone VARCHAR(20) UNIQUE,
    role TINYINT,                    -- 整数类型
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Java实体类字段映射
```java
@Entity
@Table(name = "system_provider")
public class Doctor {
    @Column(name = "license_id")
    private Long licenseId;           // 匹配BIGINT
    
    @Column(name = "role")
    private Integer specialization;   // 匹配TINYINT
}
```

## 专业领域编码

### 建议的专业领域编码
```java
// 专业领域编码映射
1 - 内科
2 - 外科
3 - 儿科
4 - 妇产科
5 - 眼科
6 - 耳鼻喉科
7 - 皮肤科
8 - 精神科
9 - 急诊科
10 - 其他
```

## 验证修复

### 1. 编译验证
```bash
mvn clean compile
# 应该成功编译，无错误
```

### 2. 运行验证
```bash
mvn spring-boot:run
# 应该成功启动，无数据库类型错误
```

### 3. 数据验证
```sql
-- 检查插入的数据
SELECT * FROM system_provider;
-- license_id应该是数字，role应该是数字
```

## 注意事项

1. **类型一致性**：确保Java字段类型与数据库字段类型匹配
2. **数据转换**：前端传入的字符串需要转换为相应的数字类型
3. **验证规则**：更新前端验证规则以匹配新的数据类型
4. **API文档**：更新API文档以反映字段类型变化

## 前端适配建议

前端需要相应调整：
```javascript
// 医生注册数据
const doctorData = {
    username: "testdoctor",
    password: "password123",
    licenseId: 123456789,        // 数字类型
    name: "测试医生",
    phone: "13900139000",
    specialization: 1            // 数字类型
};
```

现在数据库字段类型不匹配的问题已经修复！
