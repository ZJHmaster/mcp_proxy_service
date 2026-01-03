package com.example.mcpproxy.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 响应数据
     */
    private String data;

    /**
     * 错误信息（如果有）
     */
    private String error;

    /**
     * 时间戳
     */
    private String timestamp;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将响应转换为 JSON 字符串
     */
    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return String.format("{\"success\":%s,\"data\":\"%s\",\"error\":\"%s\",\"timestamp\":\"%s\"}",
                    success, data, error, timestamp);
        }
    }
}
