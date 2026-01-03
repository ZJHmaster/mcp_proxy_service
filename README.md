# MCP Proxy Service

基于 **Spring Boot + Spring AI + MCP** 的内部 API 代理服务，支持流式输出。

## 🌟 功能特性

- ✅ **MCP Server 支持** - 基于 Spring AI MCP Server
- ✅ **流式输出** - 支持 Server-Sent Events (SSE)
- ✅ **内部 API 转发** - 将 MCP 请求转发到公司内部接口
- ✅ **Demo/Mock 模式** - 无需后端即可测试
- ✅ **Docker 支持** - 适用于模搭社区部署
- ✅ **健康检查** - Actuator 端点

## 📁 项目结构

```
mcp-proxy-service/
├── pom.xml                              # Maven 配置
├── Dockerfile                           # Docker 构建配置
├── README.md                            # 项目说明
└── src/main/
    ├── java/com/example/mcpproxy/
    │   ├── McpProxyApplication.java     # 启动类
    │   ├── config/
    │   │   └── McpServerConfig.java     # MCP 配置
    │   ├── tools/
    │   │   ├── InternalApiTool.java     # 内部 API 工具
    │   │   └── DemoTool.java            # 演示工具
    │   ├── service/
    │   │   └── InternalApiService.java  # API 调用服务
    │   └── dto/
    │       ├── ApiRequest.java          # 请求 DTO
    │       └── ApiResponse.java         # 响应 DTO
    └── resources/
        └── application.yml              # 应用配置
```

## 🚀 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- Docker (可选，用于容器化部署)

### 本地运行

```bash
# 构建项目
mvn clean package

# 运行应用
java -jar target/mcp-proxy-service-1.0.0-SNAPSHOT.jar

# 或使用 Maven 运行
mvn spring-boot:run
```

### Docker 运行

```bash
# 构建镜像
docker build -t mcp-proxy-service:latest .

# 运行容器
docker run -d -p 8080:8080 \
  -e MOCK_ENABLED=true \
  -e INTERNAL_API_BASE_URL=http://your-internal-api:9000 \
  --name mcp-proxy \
  mcp-proxy-service:latest
```

## 🔌 MCP 端点

| 端点 | 方法 | 说明 |
|------|------|------|
| `/sse` | GET | MCP SSE 连接端点 |
| `/mcp/message` | POST | MCP 消息处理端点 |
| `/actuator/health` | GET | 健康检查 |

## 🛠️ 可用工具

### 内部 API 工具 (InternalApiTool)

| 工具名 | 说明 | 参数 |
|--------|------|------|
| `callInternalApi` | 同步调用内部 API | endpoint, method, body |
| `callInternalApiStream` | 流式调用内部 API | endpoint, prompt |

### 演示工具 (DemoTool)

| 工具名 | 说明 | 参数 |
|--------|------|------|
| `greet` | 问候测试 | name |
| `getSystemInfo` | 获取系统信息 | 无 |
| `streamDemo` | 流式输出演示 | text |
| `calculate` | 数学计算 | num1, operator, num2 |

## ⚙️ 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `SERVER_PORT` | `8080` | 服务端口 |
| `INTERNAL_API_BASE_URL` | `http://localhost:9000` | 内部 API 基础 URL |
| `MOCK_ENABLED` | `true` | 是否启用模拟模式 |

## 📦 模搭社区部署

### 1. 构建并推送镜像

```bash
# 构建镜像
docker build -t registry.cn-hangzhou.aliyuncs.com/your-namespace/mcp-proxy-service:latest .

# 推送到阿里云镜像仓库
docker push registry.cn-hangzhou.aliyuncs.com/your-namespace/mcp-proxy-service:latest
```

### 2. 在模搭社区创建应用

1. 登录模搭社区
2. 创建新的模型服务
3. 选择自定义镜像部署
4. 填写镜像地址和环境变量
5. 启动服务

### 3. 配置环境变量

```yaml
MOCK_ENABLED: "false"
INTERNAL_API_BASE_URL: "http://your-company-api.internal:8080"
```

## 🧪 测试 MCP 连接

使用任何支持 MCP 的客户端连接到服务：

```json
{
  "mcpServers": {
    "internal-api-proxy": {
      "url": "http://localhost:8080/sse"
    }
  }
}
```

## 📝 开发指南

### 添加新工具

1. 在 `tools` 包下创建新的 Tool 类
2. 使用 `@Tool` 注解标记方法
3. 在 `McpServerConfig` 中注册

```java
@Component
public class MyNewTool {
    
    @Tool(description = "工具描述")
    public String myMethod(@ToolParam(description = "参数描述") String param) {
        // 实现逻辑
        return "result";
    }
}
```

### 支持流式输出

返回 `Flux<String>` 类型即可自动支持流式输出：

```java
@Tool(description = "流式工具")
public Flux<String> streamMethod(@ToolParam(description = "参数") String param) {
    return Flux.just("chunk1", "chunk2", "chunk3")
            .delayElements(Duration.ofMillis(100));
}
```

## 📄 License

MIT License
