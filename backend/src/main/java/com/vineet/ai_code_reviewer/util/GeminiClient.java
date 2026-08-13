package com.vineet.ai_code_reviewer.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.vineet.ai_code_reviewer.dto.GeminiRequest;
import com.vineet.ai_code_reviewer.dto.GeminiResponse;
import com.vineet.ai_code_reviewer.entity.IssueCategory;
import com.vineet.ai_code_reviewer.entity.Review;
import com.vineet.ai_code_reviewer.entity.ReviewIssue;
import com.vineet.ai_code_reviewer.entity.Severity;
import com.vineet.ai_code_reviewer.exception.GeminiApiException;
import com.vineet.ai_code_reviewer.exception.MalformedGeminiResponseException;

import tools.jackson.databind.ObjectMapper;

@Component
public class GeminiClient {

    private final RestClient restClient;
    private final GeminiSchemaProvider schemaProvider;
    private final ReviewPromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;
    private DiffSizeValidator diffSizeValidator;

    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String geminiUrl;

    public GeminiClient(RestClient.Builder builder,
                         GeminiSchemaProvider schemaProvider,
                         ReviewPromptBuilder promptBuilder,
                         ObjectMapper objectMapper,
                         DiffSizeValidator diffSizeValidator) {
        this.restClient = builder
            .baseUrl("https://generativelanguage.googleapis.com/v1beta")
            .build();
        this.schemaProvider = schemaProvider;
        this.promptBuilder = promptBuilder;
        this.objectMapper = objectMapper;
        this.diffSizeValidator = diffSizeValidator;
    }

    public List<ReviewIssue> analyzeDiff(String diff, Review review) {
    	diffSizeValidator.validate(diff);
        String prompt = promptBuilder.build(diff);

        GeminiRequest request = new GeminiRequest(
            List.of(new GeminiRequest.Content("user", List.of(new GeminiRequest.Part(prompt)))),
            new GeminiRequest.GenerationConfig("application/json", schemaProvider.issueListSchema())
        );

        GeminiResponse response = restClient.post()
        	.uri(geminiUrl + "?key={key}", apiKey)
        	.contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (req, res) -> {
                throw new GeminiApiException("Gemini API error: " + res.getStatusCode());
            })
            .body(GeminiResponse.class);

        String jsonText = response.candidates().get(0).content().parts().get(0).text();

        return parseIssues(jsonText, review);
    }

    private List<ReviewIssue> parseIssues(String jsonText, Review review) {
        try {
            GeminiIssuesPayload payload = objectMapper.readValue(jsonText, GeminiIssuesPayload.class);

            return payload.issues().stream()
                .map(dto -> toEntity(dto, review))
                .toList();

        } catch (Exception e) {
            throw new MalformedGeminiResponseException("Failed to parse Gemini response", e);
         }
    }

    private ReviewIssue toEntity(GeminiIssueDto dto, Review review) {
        ReviewIssue issue = new ReviewIssue();
        issue.setReview(review);
        issue.setFilePath(dto.filePath());
        issue.setDiffPosition(dto.diffPosition());
        issue.setSeverity(dto.severity());
        issue.setCategory(dto.category());
        issue.setMessage(dto.message());
        return issue;
    }
}

record GeminiIssuesPayload(List<GeminiIssueDto> issues) {}
record GeminiIssueDto(String filePath, Integer diffPosition, Severity severity, IssueCategory category, String message) {}
