# Amore Blog Backend

Spring Boot + MyBatis + Maven + Oracle

## 🚀 快速开始
### 1 环境准备
+ JDK 17+
+ Maven 3.9+（推荐使用项目自带的 `mvnw` / `mvnw.cmd`）
+ 数据库（默认使用 Oracle，可自定切换）

### 2 构建与运行

检查 .mvn/wrapper/maven-wrapper.properties 里指定的 Maven 版本。编译、启动 SpringBoot 项目：
```shell
./mvnw.cmd spring-boot:run
```

## 📃 计划功能
+ 用户登录、注册、密码找回、登出（JWT）；
+ 博文发布、编辑、删除；
+ 博文评论、点赞、收藏；
+ 管理后台；
+ 用户聊天；