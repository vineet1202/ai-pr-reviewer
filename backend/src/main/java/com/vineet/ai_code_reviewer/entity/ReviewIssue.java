package com.vineet.ai_code_reviewer.entity;

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
@Table(name = "review_issues")
public class ReviewIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    private String filePath;

    private Integer diffPosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private IssueCategory category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getDiffPosition() {
		return diffPosition;
	}

	public void setDiffPosition(Integer diffPosition) {
		this.diffPosition = diffPosition;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public IssueCategory getCategory() {
		return category;
	}

	public void setCategory(IssueCategory category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReviewIssue(Long id, Review review, String filePath, Integer diffPosition, Severity severity,
			IssueCategory category, String message) {
		this.id = id;
		this.review = review;
		this.filePath = filePath;
		this.diffPosition = diffPosition;
		this.severity = severity;
		this.category = category;
		this.message = message;
	}

	public ReviewIssue() {
		super();
	}
    
    
}