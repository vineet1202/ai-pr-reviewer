package com.vineet.ai_code_reviewer.dto;

import java.time.Instant;
import java.util.List;

import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.entity.ReviewIssue;
import com.vineet.ai_code_reviewer.entity.ReviewStatus;

public class ReviewResponse {

    private Long id;
    private String prUrl;
    private String repoOwner;
    private String repoName;
    private Integer prNumber;
    private ReviewStatus status;
    private String failureReason;
    private Instant createdAt;
    private Instant completedAt;
    private List<ReviewIssueResponse> issues; // null/empty until COMPLETED

    public static ReviewResponse from(Review review) {
        ReviewResponse dto = new ReviewResponse();
        dto.id = review.getId();
        dto.prUrl = review.getPrUrl();
        dto.repoOwner = review.getRepoOwner();
        dto.repoName = review.getRepoName();
        dto.prNumber = review.getPrNumber();
        dto.status = review.getStatus();
        dto.failureReason = review.getFailureReason();
        dto.createdAt = review.getCreatedAt();
        dto.completedAt = review.getCompletedAt();
        return dto;
    }

    public static ReviewResponse from(Review review, List<ReviewIssue> issues) {
        ReviewResponse dto = from(review);
        dto.issues = issues.stream().map(ReviewIssueResponse::from).toList();
        return dto;
    }

	public Long getId() {
		return id;
	}

	public String getPrUrl() {
		return prUrl;
	}

	public String getRepoOwner() {
		return repoOwner;
	}

	public String getRepoName() {
		return repoName;
	}

	public Integer getPrNumber() {
		return prNumber;
	}

	public ReviewStatus getStatus() {
		return status;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getCompletedAt() {
		return completedAt;
	}

	public List<ReviewIssueResponse> getIssues() {
		return issues;
	}

    
    
}