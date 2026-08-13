package com.vineet.ai_code_reviewer.dto;

import com.vineet.ai_code_reviewer.entity.IssueCategory;
import com.vineet.ai_code_reviewer.entity.ReviewIssue;
import com.vineet.ai_code_reviewer.entity.Severity;

public class ReviewIssueResponse {
    private String filePath;
    private Integer diffPosition;
    private Severity severity;
    private IssueCategory category;
    private String message;

    public static ReviewIssueResponse from(ReviewIssue issue) {
        ReviewIssueResponse dto = new ReviewIssueResponse();
        dto.filePath = issue.getFilePath();
        dto.diffPosition = issue.getDiffPosition();
        dto.severity = issue.getSeverity();
        dto.category = issue.getCategory();
        dto.message = issue.getMessage();
        return dto;
    }

	public String getFilePath() {
		return filePath;
	}

	public Integer getDiffPosition() {
		return diffPosition;
	}

	public Severity getSeverity() {
		return severity;
	}

	public IssueCategory getCategory() {
		return category;
	}

	public String getMessage() {
		return message;
	}
    
    
}