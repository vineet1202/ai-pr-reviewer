package com.vineet.ai_code_reviewer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vineet.ai_code_reviewer.dto.ReviewResponse;
import com.vineet.ai_code_reviewer.dto.SubmitReviewRequest;
import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> submitReview(
            @RequestBody @Valid SubmitReviewRequest request,
            @AuthenticationPrincipal OAuth2User principal) {

        Review review = reviewService.submitReview(request.getPrUrl(), principal);
        return ResponseEntity.accepted().body(ReviewResponse.from(review));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }
    
    @PostMapping("/{id}/retry")
    public ResponseEntity<ReviewResponse> retryReview(@PathVariable Long id) {
        Review review = reviewService.retryReview(id);
        return ResponseEntity.accepted().body(ReviewResponse.from(review));
    }
    
    @GetMapping
	public String health() {
		return "OK";
	}
}
