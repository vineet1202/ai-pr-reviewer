package com.vineet.ai_code_reviewer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SubmitReviewRequest {

    @NotBlank(message = "PR URL is required")
    @Pattern(
        regexp = "^https://github\\.com/[^/]+/[^/]+/pull/\\d+$",
        message = "Must be a valid GitHub PR URL"
    )
    private String prUrl;

    public String getPrUrl() { return prUrl; }
    public void setPrUrl(String prUrl) { this.prUrl = prUrl; }
}