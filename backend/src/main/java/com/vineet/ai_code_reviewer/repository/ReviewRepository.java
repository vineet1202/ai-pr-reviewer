package com.vineet.ai_code_reviewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserOrderByCreatedAtDesc(User user);
}
