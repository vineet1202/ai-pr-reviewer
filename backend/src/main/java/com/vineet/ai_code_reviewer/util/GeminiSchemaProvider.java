package com.vineet.ai_code_reviewer.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GeminiSchemaProvider {

    public Map<String, Object> issueListSchema() {
        return Map.of(
            "type", "OBJECT",
            "properties", Map.of(
                "issues", Map.of(
                    "type", "ARRAY",
                    "items", Map.of(
                        "type", "OBJECT",
                        "properties", Map.of(
                            "filePath", Map.of("type", "STRING"),
                            "diffPosition", Map.of("type", "INTEGER"),
                            "severity", Map.of("type", "STRING", "enum", List.of("HIGH", "MEDIUM", "LOW")),
                            "category", Map.of("type", "STRING",
                                "enum", List.of("BUG", "SECURITY", "STYLE", "PERFORMANCE", "MAINTAINABILITY")),
                            "message", Map.of("type", "STRING")
                        ),
                        "required", List.of("filePath", "severity", "category", "message")
                    )
                )
            ),
            "required", List.of("issues")
        );
    }
}