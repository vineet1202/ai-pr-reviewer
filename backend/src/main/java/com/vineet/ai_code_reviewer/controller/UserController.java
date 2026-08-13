package com.vineet.ai_code_reviewer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
	 @GetMapping("/me")
	    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
	        if (principal == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated"));
	        }

	        Map<String, Object> user = new HashMap<>();
	        user.put("login", principal.getAttribute("login"));
	        user.put("name", principal.getAttribute("name"));
	        user.put("avatarUrl", principal.getAttribute("avatar_url"));
	        user.put("email", principal.getAttribute("email"));

	        return ResponseEntity.ok(user);
	    }
}
