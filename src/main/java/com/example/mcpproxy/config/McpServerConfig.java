package com.example.mcpproxy.config;

import com.example.mcpproxy.tools.DemoTool;
import com.example.mcpproxy.tools.InternalApiTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server 配置类
 * 注册所有可用的 Tool 到 MCP Server
 */
@Configuration
public class McpServerConfig {

    /**
     * 注册所有工具到 MCP Server
     * 使用 MethodToolCallbackProvider 将 @Tool 注解的方法暴露给 MCP 客户端
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(InternalApiTool internalApiTool, DemoTool demoTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(internalApiTool, demoTool)
                .build();
    }
}
