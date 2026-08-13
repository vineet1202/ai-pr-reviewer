package com.vineet.ai_code_reviewer.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.vineet.ai_code_reviewer.entity.User;
import com.vineet.ai_code_reviewer.repository.UserRepository;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RestClient githubClient;

    public GithubOAuth2UserService(UserRepository userRepository, RestClient.Builder builder) {
        this.userRepository = userRepository;
        this.githubClient = builder.baseUrl("https://api.github.com").build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User principal = super.loadUser(userRequest);
        Object rawId = principal.getAttribute("id");
        Object rawLogin = principal.getAttribute("login");
        if (!(rawId instanceof Number githubId) || !(rawLogin instanceof String login) || login.isBlank()) {
            throw new OAuth2AuthenticationException("GitHub did not return a valid user profile");
        }

        String accessToken = userRequest.getAccessToken().getTokenValue();
        User user = userRepository.findByGithubId(githubId.longValue()).orElseGet(User::new);
        user.setGithubId(githubId.longValue());
        user.setGithubUsername(login);
        user.setAvatarUrl(principal.getAttribute("avatar_url"));
        user.setEmail(findEmail(principal, accessToken));
        user.setAccessToken(accessToken);
        userRepository.save(user);
        return principal;
    }

    private String findEmail(OAuth2User principal, String accessToken) {
        String profileEmail = principal.getAttribute("email");
        if (profileEmail != null && !profileEmail.isBlank()) {
            return profileEmail;
        }
        GithubEmail[] emails = githubClient.get()
            .uri("/user/emails")
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2026-03-10")
            .retrieve()
            .body(GithubEmail[].class);
        if (emails != null) {
            for (GithubEmail email : emails) {
                if (email.primary() && email.verified()) {
                    return email.email();
                }
            }
        }
        throw new OAuth2AuthenticationException("GitHub did not provide a verified primary email");
    }

    private record GithubEmail(String email, boolean primary, boolean verified) {
    }
}
