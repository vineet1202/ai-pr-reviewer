package com.vineet.ai_code_reviewer.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.vineet.ai_code_reviewer.dto.ReviewRequest;
import com.vineet.ai_code_reviewer.dto.ReviewResponse;
import com.vineet.ai_code_reviewer.entity.Review;

public interface ReviewService {
    
    public Review submitReview(String prUrl, OAuth2User principal);
    
    public ReviewResponse getReview(Long id);
    
    public Review retryReview(Long id);
}