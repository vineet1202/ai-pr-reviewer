package com.vineet.ai_code_reviewer.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String prUrl;

    private String repoOwner;
    private String repoName;
    private Integer prNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;   // PENDING, IN_PROGRESS, COMPLETED, FAILED

    private String failureReason;  // nullable, populated only if FAILED

    @CreationTimestamp
    private Instant createdAt;

    private Instant completedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPrUrl() {
		return prUrl;
	}

	public void setPrUrl(String prUrl) {
		this.prUrl = prUrl;
	}

	public String getRepoOwner() {
		return repoOwner;
	}

	public void setRepoOwner(String repoOwner) {
		this.repoOwner = repoOwner;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public Integer getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(Integer prNumber) {
		this.prNumber = prNumber;
	}

	public ReviewStatus getStatus() {
		return status;
	}

	public void setStatus(ReviewStatus status) {
		this.status = status;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Instant completedAt) {
		this.completedAt = completedAt;
	}

	public Review(Long id, User user, String prUrl, String repoOwner, String repoName, Integer prNumber,
			ReviewStatus status, String failureReason, Instant createdAt, Instant completedAt) {
		this.id = id;
		this.user = user;
		this.prUrl = prUrl;
		this.repoOwner = repoOwner;
		this.repoName = repoName;
		this.prNumber = prNumber;
		this.status = status;
		this.failureReason = failureReason;
		this.createdAt = createdAt;
		this.completedAt = completedAt;
	}

	public Review() {
		super();
	}
    
    
}
