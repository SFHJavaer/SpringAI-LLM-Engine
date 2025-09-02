# SpringAI LLM Engine

基于Spring AI的智能对话引擎，支持多种大语言模型和RAG知识库检索。

## 🚀 功能特性

- 🤖 支持多种AI模型（OpenAI、DeepSeek等）
- 🔍 RAG知识库检索
- 🌐 实时网络搜索
- 🔐 JWT用户认证
- 📊 Redis向量存储
- 💾 MySQL数据持久化

## 📋 环境要求

- Java 21+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

## 🛠️ 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/your-username/springai-llm-engine.git
cd springai-llm-engine
```

### 2. 配置环境变量
```bash
# 复制环境变量模板
cp env.example .env

# 编辑.env文件，填入你的配置信息
vim .env
```

### 3. 配置数据库
```sql
-- 创建数据库
CREATE DATABASE spring_ai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. 运行项目
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

## ⚙️ 配置说明

### 必需的环境变量

| 变量名 | 说明 | 示例 |
|--------|------|------|
| `OPENAI_API_KEY` | OpenAI API密钥 | `sk-...` |
| `DEEPSEEK_API_KEY` | DeepSeek API密钥 | `sk-...` |
| `DB_HOST` | 数据库主机 | `localhost` |
| `DB_PASSWORD` | 数据库密码 | `your-password` |
| `REDIS_PASSWORD` | Redis密码 | `your-redis-password` |
| `JWT_SECRET` | JWT密钥 | `your-jwt-secret` |

### 可选的环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `DB_PORT` | `3306` | 数据库端口 |
| `DB_NAME` | `spring_ai` | 数据库名称 |
| `REDIS_HOST` | `localhost` | Redis主机 |
| `REDIS_PORT` | `6379` | Redis端口 |
| `JWT_EXPIRATION` | `604800000` | JWT过期时间(毫秒) |

## 📁 项目结构

```
SpringAI-LLM-Engine/
├── mcp-client/                 # 主应用模块
│   ├── src/main/java/
│   │   └── com/sfh/
│   │       ├── config/         # 配置类
│   │       ├── controller/     # 控制器
│   │       ├── entity/         # 实体类
│   │       ├── service/        # 服务层
│   │       └── util/           # 工具类
│   └── src/main/resources/
│       ├── application.yml     # 主配置文件
│       └── application-template.yml  # 配置模板
├── env.example                 # 环境变量示例
└── README.md                   # 项目说明
```

## 🔧 API接口

### 用户认证
- `POST /auth/register` - 用户注册
- `POST /auth/login` - 用户登录
- `GET /auth/validate` - 验证token

### 对话接口
- `POST /chat/doChat` - 发送消息
- `POST /rag/search` - RAG知识库检索
- `POST /search/internet` - 网络搜索

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🙏 致谢

- [Spring AI](https://spring.io/projects/spring-ai)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Hutool](https://hutool.cn/)

## 📞 联系方式

- 项目主页: https://github.com/sfhjavaer/springai-llm-engine
- 问题反馈: https://github.com/sfhjavaer/springai-llm-engine/issues
