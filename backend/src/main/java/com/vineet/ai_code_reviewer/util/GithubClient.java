package com.vineet.ai_code_reviewer.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.exception.GitHubApiException;

@Component
public class GithubClient {

    private final RestClient restClient;

    public GithubClient(RestClient.Builder builder) {
        this.restClient = builder
            .baseUrl("https://api.github.com")
            .build();
    }

    public String fetchDiff(Review review, String accessToken) {
        GitHubFile[] files = restClient.get()
            .uri("/repos/{owner}/{repo}/pulls/{number}/files?per_page=100",
                review.getRepoOwner(), review.getRepoName(), review.getPrNumber())
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2026-03-10")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                throw new GitHubApiException("GitHub API error: " + res.getStatusCode());
            })
            .body(GitHubFile[].class);

        if (files == null) {
            throw new GitHubApiException("GitHub returned no files for this pull request");
        }

        return Arrays.stream(files)
            .filter(f -> f.patch() != null)
            .map(f -> "File: " + f.filename() + "\n" + f.patch())
            .collect(Collectors.joining("\n\n"));
    }
}

record GitHubFile(String filename, String patch) {}
