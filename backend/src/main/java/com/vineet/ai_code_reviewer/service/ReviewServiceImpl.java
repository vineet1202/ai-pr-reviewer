package com.vineet.ai_code_reviewer.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.vineet.ai_code_reviewer.dto.ParsedPrUrl;
import com.vineet.ai_code_reviewer.dto.ReviewRequest;
import com.vineet.ai_code_reviewer.dto.ReviewResponse;
import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.entity.ReviewStatus;
import com.vineet.ai_code_reviewer.entity.User;
import com.vineet.ai_code_reviewer.exception.InvalidReviewStateException;
import com.vineet.ai_code_reviewer.exception.ReviewNotFoundException;
import com.vineet.ai_code_reviewer.repository.ReviewIssueRepository;
import com.vineet.ai_code_reviewer.repository.ReviewRepository;
import com.vineet.ai_code_reviewer.repository.UserRepository;
import com.vineet.ai_code_reviewer.util.GithubUrlParser;

import jakarta.transaction.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final GithubUrlParser urlParser;
    private final ReviewProcessor reviewProcessor;
    private final UserRepository userRepository;
    private final ReviewIssueRepository issueRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, GithubUrlParser urlParser,
            ReviewProcessor reviewProcessor, UserRepository userRepository, ReviewIssueRepository issueRepository) {
        this.reviewRepository = reviewRepository;
        this.urlParser = urlParser;
        this.reviewProcessor = reviewProcessor;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    @Transactional
    public Review submitReview(String prUrl, OAuth2User principal) {
        ParsedPrUrl parsed = urlParser.parse(prUrl);
        User user = resolveUser(principal);

        Review review = new Review();
        review.setUser(user);
        review.setPrUrl(prUrl);
        review.setRepoOwner(parsed.owner());
        review.setRepoName(parsed.repo());
        review.setPrNumber(parsed.number());
        review.setStatus(ReviewStatus.PENDING);

        Review saved = reviewRepository.save(review);
        processAfterCommit(saved.getId());
        return saved;
    }

    @Override
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException(id));
        return ReviewResponse.from(review, issueRepository.findByReview(review));
    }

    @Override
    @Transactional
    public Review retryReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException(id));

        if (review.getStatus() != ReviewStatus.FAILED) {
            throw new InvalidReviewStateException(
                "Only FAILED reviews can be retried, current status: " + review.getStatus());
        }

        issueRepository.deleteByReview(review);
        review.setFailureReason(null);
        review.setStatus(ReviewStatus.PENDING);
        reviewRepository.save(review);
        processAfterCommit(review.getId());
        return review;
    }

    private void processAfterCommit(Long reviewId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            reviewProcessor.processAsync(reviewId);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                reviewProcessor.processAsync(reviewId);
            }
        });
    }

    private User resolveUser(OAuth2User principal) {
        Object githubId = principal.getAttribute("id");
        if (!(githubId instanceof Number id)) {
            throw new IllegalStateException("Authenticated GitHub user has no numeric id");
        }
        return userRepository.findByGithubId(id.longValue())
            .orElseThrow(() -> new IllegalStateException("GitHub user has not been persisted during login"));
    }
}
