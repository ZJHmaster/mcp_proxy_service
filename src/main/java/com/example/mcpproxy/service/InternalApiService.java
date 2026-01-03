package com.example.mcpproxy.service;

import com.example.mcpproxy.dto.ApiRequest;
import com.example.mcpproxy.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 内部 API 调用服务
 * 负责与公司内部接口通信，支持同步和流式调用
 */
@Slf4j
@Service
public class InternalApiService {

    private final WebClient webClient;
    private final boolean mockEnabled;

    public InternalApiService(
            @Value("${internal.api.base-url}") String baseUrl,
            @Value("${internal.api.timeout}") int timeout,
            @Value("${internal.api.mock-enabled}") boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        log.info("InternalApiService initialized with baseUrl: {}, mockEnabled: {}", baseUrl, mockEnabled);
    }

    /**
     * 同步调用内部 API
     */
    public ApiResponse callApi(ApiRequest request) {
        if (mockEnabled) {
            return mockApiResponse(request);
        }

        try {
            HttpMethod method = HttpMethod.valueOf(request.getMethod().toUpperCase());

            String responseBody = webClient
                    .method(method)
                    .uri(request.getEndpoint())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request.getBody() != null ? request.getBody() : "")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));

            return ApiResponse.builder()
                    .success(true)
                    .data(responseBody)
                    .timestamp(LocalDateTime.now().toString())
                    .build();

        } catch (Exception e) {
            log.error("Error calling internal API: {}", request, e);
            return ApiResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .timestamp(LocalDateTime.now().toString())
                    .build();
        }
    }

    /**
     * 流式调用内部 API
     */
    public Flux<String> callApiStream(String endpoint, String prompt) {
        if (mockEnabled) {
            return mockStreamResponse(prompt);
        }

        return webClient
                .post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("prompt", prompt, "stream", true))
                .retrieve()
                .bodyToFlux(String.class)
                .timeout(Duration.ofSeconds(60))
                .doOnError(e -> log.error("Stream error: ", e));
    }

    /**
     * 模拟 API 响应（用于 Demo）
     */
    private ApiResponse mockApiResponse(ApiRequest request) {
        log.info("Mock mode: generating mock response for {}", request);

        String mockData = String.format("""
                {
                    "message": "这是模拟响应",
                    "endpoint": "%s",
                    "method": "%s",
                    "receivedBody": %s,
                    "note": "当前为 Demo 模式，实际部署时请配置 INTERNAL_API_BASE_URL 并设置 MOCK_ENABLED=false"
                }
                """,
                request.getEndpoint(),
                request.getMethod(),
                request.getBody() != null ? request.getBody() : "null");

        return ApiResponse.builder()
                .success(true)
                .data(mockData)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    /**
     * 模拟流式响应（用于 Demo）
     */
    private Flux<String> mockStreamResponse(String prompt) {
        log.info("Mock mode: generating mock stream response for prompt: {}", prompt);

        String response = String.format(
                "收到您的请求：「%s」\n\n" +
                        "这是一个模拟的流式响应。在实际部署时，这里会连接到您公司的内部 API。\n\n" +
                        "当前系统状态：正常运行中。\n" +
                        "时间：%s",
                prompt,
                LocalDateTime.now());

        // 模拟流式输出，每50ms输出一个字符
        return Flux.fromArray(response.split(""))
                .delayElements(Duration.ofMillis(50));
    }
}
