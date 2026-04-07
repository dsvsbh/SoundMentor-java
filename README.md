# SoundMentor 后端项目

## 项目简介

SoundMentor后端项目是基于Java和Spring Boot开发的智能语音教辅系统后端服务，提供用户管理、组织管理、有声课件生成、语音阅读辅助等核心功能的API接口。

## 技术栈

- **开发语言**：Java 17+
- **框架**：Spring Boot 3
- **ORM**：MyBatis-Plus
- **数据库**：MySQL
- **缓存**：Redis
- **对象存储**：MinIO
- **文档处理**：POI
- **AI服务**：阿里云DashScope
- **构建工具**：Maven
- **容器化**：Docker

## 项目结构

```
SoundMentor-java/
├── SoundMentor-base/         # 基础模块
│   ├── src/main/java/com/soundmentor/soundmentorbase/
│   │   ├── constants/         # 常量定义
│   │   ├── enums/             # 枚举类
│   │   ├── exception/         # 异常处理
│   │   └── utils/             # 工具类
│   └── pom.xml                # 模块依赖
├── SoundMentor-parent/        # 父项目
│   └── pom.xml                # 父项目依赖管理
├── SoundMentor-pojo/          # 数据模型模块
│   ├── src/main/java/com/soundmentor/soundmentorpojo/
│   │   ├── DO/                # 数据对象
│   │   └── DTO/               # 数据传输对象
│   └── pom.xml                # 模块依赖
├── SoundMentor-web/           # Web服务模块
│   ├── src/main/java/com/soundmentor/soundmentorweb/
│   │   ├── config/             # 配置类
│   │   ├── controller/         # 控制器
│   │   ├── service/            # 服务层
│   │   ├── mapper/             # 数据访问层
│   │   └── interceptor/        # 拦截器
│   ├── src/main/resources/     # 资源文件
│   │   ├── application.yml     # 应用配置
│   │   └── mapper/             # MyBatis映射文件
│   └── pom.xml                # 模块依赖
├── Dockerfile                 # Docker构建文件
├── .gitignore                 # Git忽略配置
└── README.md                  # 项目说明
```

## 核心功能

### 1. 用户管理
- 用户注册、登录
- 个人信息管理
- 密码重置
- JWT认证

### 2. 组织管理
- 组织创建与管理
- 成员邀请与管理
- 组织角色权限控制
- 组织文件共享

### 3. 声音库管理
- 预设声音管理
- 声音效果调整
- 声音参数配置

### 4. 有声课件生成
- PPT上传与解析
- 课件内容编辑
- 语音讲解生成
- 课件预览与导出

### 5. 语音阅读辅助
- 文本转语音
- 语音参数调整
- 语音文件管理

## 安装与运行

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- MinIO

### 构建项目

```bash
# 进入项目根目录
cd SoundMentor-java

# 构建项目
mvn clean package
```

### 运行服务

```bash
# 运行Web服务
java -jar SoundMentor-web/target/SoundMentor-web.jar
```

### Docker部署

```bash
# 构建Docker镜像
docker build -t soundmentor-backend .

# 运行Docker容器
docker run -p 8080:8080 --name soundmentor-backend soundmentor-backend
```

## 配置说明

### 数据库配置
在 `application.yml` 文件中配置MySQL数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/soundmentor
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
```

### MinIO配置

```yaml
minio:
  endpoint: http://localhost:9000
  accessKey: minioadmin
  secretKey: minioadmin
  bucketName: soundmentor
```

### 阿里云DashScope配置

```yaml
dashscope:
  apiKey: your_api_key
```

## API文档

项目集成了SpringDoc OpenAPI，可以通过以下地址访问API文档：

```
http://localhost:8080/swagger-ui.html
```

## 开发规范

### 代码风格
- 遵循Java编码规范
- 使用Google Style Guide
- 代码注释清晰完整

### 提交规范
- 提交信息使用语义化提交格式
- 提交前运行单元测试

### 分支管理
- `main`：主分支，用于发布
- `develop`：开发分支
- `feature/*`：功能分支
- `bugfix/*`： bug修复分支

## 日志管理

项目使用SLF4J + Logback进行日志管理，日志配置文件位于 `src/main/resources/logback.xml`。

## 监控与告警

项目集成了Spring Boot Actuator，可以通过以下地址访问监控端点：

```
http://localhost:8080/actuator
```

## 许可证

本项目采用MIT许可证。