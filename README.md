# 小智 - AI医疗智能客服系统

## 项目简介

**小智（ Xiaozhi）** 是一个基于 **LangChain4j** 框架和 **Spring Boot 3** 构建的 **AI 医疗智能客服系统**。该项目以北京协和医院为背景场景，打造了一个集 **智能问答、AI 分导诊、预约挂号** 为一体的医疗伴诊助手。

系统通过接入阿里云通义千问大模型，结合 **RAG（检索增强生成）**、**Function Calling（工具调用）**、**对话记忆管理** 等 AI 高级能力，为患者提供专业、友好、便捷的医疗服务体验。

---

## 技术栈

| 类别 | 技术 | 说明 |
|------|------|------|
| **基础框架** | Spring Boot 3.2.6 | 应用核心框架 |
| **Java 版本** | JDK 17 | 运行环境 |
| **AI 框架** | LangChain4j 1.4.0 | 大模型应用开发框架 |
| **大模型** | 通义千问（qwen-max / qwen-plus） | 阿里云百炼平台 |
| **兼容模型** | DeepSeek-V3 / DeepSeek-R1 | 通过 OpenAI 兼容接口接入 |
| **向量数据库** | Pinecone | 云端向量存储，用于 RAG 检索 |
| **聊天记忆存储** | MongoDB | 多用户会话记忆持久化 |
| **关系数据库** | MySQL + MyBatis-Plus | 预约挂号数据持久化 |
| **API 文档** | Knife4j (OpenAPI 3) | 接口文档与调试 |
| **服务发现** | Nacos | 微服务注册中心 |
| **HTTP 客户端** | Apache HttpClient | 远程服务接口调用 |

---

## 核心功能模块

### 1. 智能对话系统 🤖

系统提供流式（Streaming）对话接口，支持实时返回 AI 回复内容：

- **统一入口**：`POST /xiaozhi/chat`
- **流式输出**：基于 `Flux<String>` 实现 SSE 流式响应
- **多轮对话**：支持上下文记忆的多轮交互

### 2. AI 分导诊 🏥

根据患者的病情描述和就医需求，智能推荐最合适的就诊科室，帮助患者快速找到正确的就医方向。

### 3. 预约挂号助手 📋

完整的预约挂号功能闭环：

| 功能 | 说明 |
|------|------|
| **查询号源** | 远程调用医院微服务接口，查询指定科室/医生/时间的号源情况 |
| **预约挂号** | 用户确认信息后执行预约操作，自动校验重复预约 |
| **取消预约** | 支持根据条件查询并取消已有预约记录 |

> 预约功能通过 **LangChain4j 的 @Tool 注解**实现 Function Calling，AI 可自主判断何时调用工具。

### 4. RAG 检索增强生成 📚

集成 **Pinecone 向量数据库**，实现知识库检索增强：

- 使用阿里云通用文本向量模型（text-embedding-v3）生成嵌入向量
- 将文档向量化后存入 Pinecone
- 对话时自动检索相关知识片段，辅助 AI 生成更准确的回答
- 配置了最小相似度阈值（0.8），保证检索质量

### 5. 对话记忆管理 💾

基于 MongoDB 实现多用户独立的会话记忆持久化：

- 每个用户（memoryId）拥有独立的对话窗口
- 采用 **MessageWindowChatMemory** 策略，保留最近 10 条消息
- 会话数据存储在 MongoDB 的 `chat_memory_db` 数据库中
- 支持会话的增删改查操作

---

## 项目结构

```
langchain4j-ai/
├── src/main/java/com/itjaing/java/
│   ├── XiaoZhiApp.java                          # 启动类
│   ├── agent/
│   │   └── XiaozhiAgent.java                    # 核心 Agent 接口（RAG + 工具 + 记忆）
│   ├── assistant/
│   │   ├── Assistant.java                       # 基础聊天助手
│   │   ├── MemoryChatAssistant.java             # 带记忆的聊天助手
│   │   └── SeparateChatAssistant.java           # 分离式多用户聊天助手
│   ├── bean/
│   │   ├── ChatForm.java                        # 对话请求表单
│   │   └── ChatMessages.java                    # MongoDB 聊天消息实体
│   ├── config/
│   │   ├── EmbeddingStoreConfig.java            # Pinecone 向量存储配置
│   │   ├── MemoryChatAssistantConfig.java       # 记忆聊天配置
│   │   ├── SeparateChatAssistantConfig.java     # 分离聊天配置
│   │   └── XiaozhiAgentConfig.java              # 核心 Agent 配置
│   ├── controller/
│   │   └── XiaozhiController.java               # API 控制器
│   ├── entity/
│   │   └── Appointment.java                     # 预约实体
│   ├── mapper/
│   │   ├── AppointmentMapper.java               # MyBatis Mapper
│   │   └── xml/AppointmentMapper.xml            # Mapper XML
│   ├── service/
│   │   ├── AppointmentService.java              # 预约服务接口
│   │   └── impl/AppointmentServiceImpl.java     # 预约服务实现
│   ├── store/
│   │   └── MongoChatMemoryStore.java            # MongoDB 聊天记忆存储
│   └── tools/
│       ├── AppointmentTools.java                # 预约工具（Function Calling）
│       └── HttpUtils.java                       # HTTP 工具类
├── src/main/resources/
│   ├── application.properties                   # 应用配置文件
│   └── xiaozhi-prompt-template.txt             # 系统提示词模板
└── src/test/java/                               # 单元测试
    ├── AIServiceTest.java
    ├── AppointmentServiceTest.java
    ├── EmbeddingTest.java
    ├── LLMTest.java
    └── RAGTest.java
```

---

## 架构设计

```
┌─────────────────────────────────────────────────────────┐
│                      用户前端                            │
└─────────────────────┬───────────────────────────────────┘
                      │ POST /xiaozhi/chat (SSE Stream)
                      ▼
┌─────────────────────────────────────────────────────────┐
│              XiaozhiController (REST API)               │
└─────────────────────┬───────────────────────────────────┘
                      ▼
┌─────────────────────────────────────────────────────────┐
│              XiaozhiAgent (AI Service)                  │
│  ┌──────────────┬──────────────┬────────────────────┐   │
│  │ System Prompt│ Chat Memory  │ Content Retriever  │   │
│  │  (角色设定)   │  (MongoDB)   │  (Pinecone RAG)    │   │
│  └──────────────┴──────────────┴────────────────────┘   │
│  ┌──────────────────────────────────────────────────┐   │
│  │         Tools (Function Calling)                  │   │
│  │  预约挂号 │ 取消预约 │ 查询号源                   │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────┬───────────────────────────────────┘
                      ▼
┌─────────────────────────────────────────────────────────┐
│              通义千问 / DeepSeek 大模型                  │
│          (qwen-max / deepseek-v3)                       │
└─────────────────────────────────────────────────────────┘
```

---

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MongoDB（聊天记忆存储）
- MySQL（预约数据存储）
- Pinecone 账号（向量数据库，可选）

### 环境变量配置

在启动前需设置以下环境变量：

```bash
# 阿里云百炼 API Key
set DASH_SCOPE_API_KEY=your_api_key

# Pinecone API Key（RAG 向量存储）
set PINECONE_API_KEY=your_pinecone_key

# DeepSeek API Key（可选）
set DEEP_SEEK_API_KEY=your_deepseek_key
```

### 数据库配置

**MySQL** - 预约挂号数据库：
```properties
spring.datasource.url=jdbc:mysql://192.168.10.101:3306/guiguxiaozhi
spring.datasource.username=root
spring.datasource.password=root
```

**MongoDB** - 聊天记忆存储：
```properties
spring.data.mongodb.uri=mongodb://192.168.10.101:27017/chat_memory_db
```

### 启动项目

```bash
mvn spring-boot:run
```

服务默认运行在 `http://localhost:8080`

### API 文档

启动后访问 Knife4j 接口文档：
```
http://localhost:8080/doc.html
```

---

## 核心接口说明

### 对话接口

**请求**
```http
POST /xiaozhi/chat
Content-Type: application/json

{
  "memoryId": 1001,
  "message": "我最近经常头痛，应该挂什么科？"
}
```

**响应**（SSE 流式输出）
```
data: 您好！我是硅谷小智，北京协和医院的智能客服~
data: 根据您描述的头痛症状，建议您挂神经内科...
```

---

## 关键技术亮点

### 1. LangChain4j AiService 声明式开发

通过注解驱动的方式定义 AI 服务，无需手动处理 prompt 拼接和模型调用：

```java
@AiService(
    wiringMode = EXPLICIT,
    chatModel = "qwenChatModel",
    streamingChatModel = "qwenStreamingChatModel",
    tools = "appointmentTools",
    contentRetriever = "contentRetrieverXiaozhiPincone"
)
public interface XiaozhiAgent {
    @SystemMessage(fromResource = "xiaozhi-prompt-template.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String message);
}
```

### 2. Function Calling 工具调用

使用 `@Tool` 注解将业务方法暴露给 AI 调用，实现自主决策的工具使用能力：

```java
@Tool(name="预约挂号", value = "根据参数...")
public String bookAppointment(Appointment appointment) { ... }
```

### 3. 多模型灵活切换

- **通义千问**：通过 DashScope 原生接口接入
- **DeepSeek**：通过 OpenAI 兼容模式接入阿里云百炼
- 配置化切换，无需修改代码

### 4. 多层次记忆管理

| 助手类型 | 记忆策略 | 存储方式 |
|----------|---------|---------|
| Assistant | 无记忆 | - |
| MemoryChatAssistant | 全局共享内存 | 内存 |
| SeparateChatAssistant | 按 memoryId 隔离 | MongoDB |
| XiaozhiAgent | 按 memoryId 隔离 + RAG | MongoDB + Pinecone |

---

## 测试覆盖

项目包含完善的单元测试，覆盖以下核心能力：

- **LLMTest**：大模型基本调用测试
- **AIServiceTest**：AI 服务集成测试
- **EmbeddingTest**：文本向量化测试
- **RAGTest**：检索增强生成测试
- **AppointmentServiceTest**：预约服务测试

---

## 作者

江小康

---

## 许可证

本项目仅供学习研究使用。
