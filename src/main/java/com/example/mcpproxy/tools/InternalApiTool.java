package com.example.mcpproxy.tools;

import com.example.mcpproxy.dto.ApiRequest;
import com.example.mcpproxy.dto.ApiResponse;
import com.example.mcpproxy.service.InternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * 内部 API 转发工具
 * 用于将 MCP 请求转发到公司内部接口
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InternalApiTool {

    private final InternalApiService internalApiService;

    /**
     * 调用内部 API（同步）
     *
     * @param endpoint API 端点路径
     * @param method   HTTP 方法 (GET, POST, PUT, DELETE)
     * @param body     请求体（JSON 格式）
     * @return API 响应结果
     */
    @Tool(description = "调用公司内部 API 接口。支持 GET、POST、PUT、DELETE 方法。返回 API 的响应结果。")
    public String callInternalApi(
            @ToolParam(description = "API 端点路径，例如: /api/users") String endpoint,
            @ToolParam(description = "HTTP 方法: GET, POST, PUT, DELETE") String method,
            @ToolParam(description = "请求体（JSON 格式），GET 请求可为空") String body) {
        log.info("Calling internal API: {} {} with body: {}", method, endpoint, body);

        ApiRequest request = ApiRequest.builder()
                .endpoint(endpoint)
                .method(method)
                .body(body)
                .build();

        ApiResponse response = internalApiService.callApi(request);

        log.info("Internal API response: {}", response);
        return response.toJson();
    }

    /**
     * 流式调用内部 API
     *
     * @param endpoint API 端点路径
     * @param prompt   请求提示词
     * @return 流式响应
     */
    @Tool(description = "流式调用公司内部 API 接口。适用于需要流式输出的场景，如聊天对话。返回流式文本响应。")
    public Flux<String> callInternalApiStream(
            @ToolParam(description = "API 端点路径，例如: /api/chat") String endpoint,
            @ToolParam(description = "请求提示词或消息内容") String prompt) {
        log.info("Streaming call to internal API: {} with prompt: {}", endpoint, prompt);

        return internalApiService.callApiStream(endpoint, prompt)
                .doOnNext(chunk -> log.debug("Stream chunk: {}", chunk))
                .doOnComplete(() -> log.info("Stream completed for endpoint: {}", endpoint))
                .doOnError(e -> log.error("Stream error for endpoint: {}", endpoint, e));
    }
}
