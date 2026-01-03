package com.example.mcpproxy.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Demo 工具类
 * 用于测试和演示 MCP 功能
 */
@Slf4j
@Component
public class DemoTool {

    /**
     * 简单的问候工具
     */
    @Tool(description = "返回一个友好的问候消息。用于测试 MCP 连接是否正常。")
    public String greet(
            @ToolParam(description = "要问候的人的名字") String name) {
        String greeting = String.format("你好，%s！欢迎使用 MCP Proxy Service。当前时间: %s",
                name,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("Generated greeting: {}", greeting);
        return greeting;
    }

    /**
     * 获取系统信息
     */
    @Tool(description = "获取 MCP Proxy Service 的系统信息，包括版本、运行状态等。")
    public String getSystemInfo() {
        Map<String, Object> info = Map.of(
                "serviceName", "MCP Proxy Service",
                "version", "1.0.0",
                "status", "running",
                "javaVersion", System.getProperty("java.version"),
                "os", System.getProperty("os.name"),
                "timestamp", LocalDateTime.now().toString());

        log.info("System info requested: {}", info);
        return info.toString();
    }

    /**
     * 流式输出演示
     */
    @Tool(description = "演示流式输出功能。会逐字返回一段文本，用于测试流式响应。")
    public Flux<String> streamDemo(
            @ToolParam(description = "要流式输出的文本内容") String text) {
        log.info("Starting stream demo with text: {}", text);

        // 将文本拆分成字符，逐个流式输出
        return Flux.fromArray(text.split(""))
                .delayElements(Duration.ofMillis(100))
                .doOnNext(c -> log.debug("Streaming char: {}", c))
                .doOnComplete(() -> log.info("Stream demo completed"));
    }

    /**
     * 计算工具演示
     */
    @Tool(description = "执行简单的数学计算。支持加、减、乘、除运算。")
    public String calculate(
            @ToolParam(description = "第一个数字") double num1,
            @ToolParam(description = "运算符: +, -, *, /") String operator,
            @ToolParam(description = "第二个数字") double num2) {
        double result;
        switch (operator) {
            case "+" -> result = num1 + num2;
            case "-" -> result = num1 - num2;
            case "*" -> result = num1 * num2;
            case "/" -> {
                if (num2 == 0) {
                    return "错误: 除数不能为零";
                }
                result = num1 / num2;
            }
            default -> {
                return "错误: 不支持的运算符 " + operator;
            }
        }

        String resultStr = String.format("%.2f %s %.2f = %.2f", num1, operator, num2, result);
        log.info("Calculation: {}", resultStr);
        return resultStr;
    }
}
