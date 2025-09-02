# SpringAI MCP Client

## 启动说明

### 1. 环境准备
- Java 21+
- Maven 3.6+
- Redis服务器
- MySQL数据库

### 2. 配置环境变量
复制 `env.example` 为 `.env` 并配置相应的API密钥和数据库连接信息。

### 3. 启动应用

#### 方式1：直接启动（推荐）
```bash
mvn spring-boot:run
```

#### 方式2：使用代理启动（如果需要访问GitHub）
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dhttp.proxyHost=your-proxy-host -Dhttp.proxyPort=your-proxy-port -Dhttps.proxyHost=your-proxy-host -Dhttps.proxyPort=your-proxy-port"
```

#### 方式3：Docker启动
```bash
docker-compose up -d
```

### 4. 故障排除

#### 网络连接问题
如果遇到无法下载transformers模型的问题，可以：

1. **禁用transformers模型**（推荐）：
   在 `application.yml` 中设置：
   ```yaml
   spring:
     ai:
       transformers:
         enabled: false
   ```

2. **配置代理**：
   在启动时添加JVM代理参数

3. **手动下载模型**：
   将模型文件放在 `./models` 目录下

#### Redis连接问题
确保Redis服务器正在运行，并检查连接配置。

#### 数据库连接问题
确保MySQL数据库正在运行，并检查连接配置。



