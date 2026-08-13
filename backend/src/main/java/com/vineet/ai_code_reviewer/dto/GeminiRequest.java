package com.vineet.ai_code_reviewer.dto;

import java.util.List;
import java.util.Map;

public record GeminiRequest(
	    List<Content> contents,
	    GenerationConfig generationConfig
	) {
	    public record Content(String role, List<Part> parts) {}
	    public record Part(String text) {}
	    public record GenerationConfig(
	        String responseMimeType,
	        Map<String, Object> responseSchema
	    ) {}
	}