# 使用官方的Java运行时作为父镜像
FROM openjdk:8-jdk

# 设置工作目录
WORKDIR /app

# 将jar包复制到容器中的工作目录
COPY app.jar app.jar

# 暴露容器中应用将要监听的端口
EXPOSE 8080

# 定义容器启动时执行的命令
ENTRYPOINT ["java","-jar","/app/app.jar"]