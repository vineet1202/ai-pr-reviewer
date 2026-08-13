package com.vineet.ai_code_reviewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.entity.ReviewIssue;

public interface ReviewIssueRepository extends JpaRepository<ReviewIssue, Long> {
    List<ReviewIssue> findByReview(Review review);
    void deleteByReview(Review review);
}

