package com.example.mcpproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MCP Proxy Service 启动类
 * 
 * 这是一个基于 Spring Boot + Spring AI MCP 的代理服务
 * 用于将 MCP 请求转发到公司内部 API
 */
@SpringBootApplication
public class McpProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpProxyApplication.class, args);
    }
}
