package com.example.mcpproxy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest {

    /**
     * API 端点路径
     */
    private String endpoint;

    /**
     * HTTP 方法 (GET, POST, PUT, DELETE)
     */
    private String method;

    /**
     * 请求体（JSON 格式）
     */
    private String body;

    /**
     * 请求头（可选）
     */
    private String headers;
}
