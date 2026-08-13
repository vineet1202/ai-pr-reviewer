package com.vineet.ai_code_reviewer.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import com.vineet.ai_code_reviewer.util.TokenEncryptionConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false)
    private String githubUsername;

    @Column(nullable = false)
    private String email;

    private String avatarUrl;

    @Convert(converter = TokenEncryptionConverter.class)
    @Column(nullable = false)
    private String accessToken;

    @CreationTimestamp
    private Instant createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGithubId() {
		return githubId;
	}

	public void setGithubId(Long githubId) {
		this.githubId = githubId;
	}

	public String getGithubUsername() {
		return githubUsername;
	}

	public void setGithubUsername(String githubUsername) {
		this.githubUsername = githubUsername;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public User(Long id, Long githubId, String githubUsername, String email, String avatarUrl, String accessToken,
			Instant createdAt) {
		this.id = id;
		this.githubId = githubId;
		this.githubUsername = githubUsername;
		this.email = email;
		this.avatarUrl = avatarUrl;
		this.accessToken = accessToken;
		this.createdAt = createdAt;
	}

	public User() {
		super();
	}
    
    
}
