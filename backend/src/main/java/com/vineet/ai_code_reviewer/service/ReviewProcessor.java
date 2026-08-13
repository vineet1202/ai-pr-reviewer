package com.vineet.ai_code_reviewer.service;

import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.entity.ReviewIssue;
import com.vineet.ai_code_reviewer.entity.ReviewStatus;
import com.vineet.ai_code_reviewer.repository.ReviewIssueRepository;
import com.vineet.ai_code_reviewer.repository.ReviewRepository;
import com.vineet.ai_code_reviewer.util.GeminiClient;
import com.vineet.ai_code_reviewer.util.GithubClient;

import jakarta.transaction.Transactional;

@Service
public class ReviewProcessor {

    private final ReviewRepository reviewRepository;
    private final GithubClient gitHubClient;
    private final GeminiClient geminiClient;
    private final ReviewIssueRepository issueRepository;

    public ReviewProcessor(ReviewRepository reviewRepository, GithubClient gitHubClient, GeminiClient geminiClient,
			ReviewIssueRepository issueRepository) {
		this.reviewRepository = reviewRepository;
		this.gitHubClient = gitHubClient;
		this.geminiClient = geminiClient;
		this.issueRepository = issueRepository;
	}

	@Async
    @Transactional
    public void processAsync(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setStatus(ReviewStatus.IN_PROGRESS);
        reviewRepository.save(review);

        try {
            String diff = gitHubClient.fetchDiff(review, review.getUser().getAccessToken());
            List<ReviewIssue> issues = geminiClient.analyzeDiff(diff, review);

            issueRepository.saveAll(issues);
            review.setStatus(ReviewStatus.COMPLETED);
            review.setCompletedAt(Instant.now());

        } catch (Exception e) {
            review.setStatus(ReviewStatus.FAILED);
            review.setFailureReason(e.getMessage());
        }

        reviewRepository.save(review);
    }
}
