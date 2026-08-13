package com.vineet.ai_code_reviewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vineet.ai_code_reviewer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGithubId(Long githubId);
}