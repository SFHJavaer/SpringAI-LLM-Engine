# 使用OpenJDK 21作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
COPY mcp-client/pom.xml mcp-client/

# 复制源代码
COPY mcp-client/src mcp-client/src

# 安装Maven
RUN apt-get update && apt-get install -y maven

# 编译项目
RUN mvn clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 设置环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动命令
CMD ["java", "-jar", "mcp-client/target/mcp-client-1.0-SNAPSHOT.jar"]
