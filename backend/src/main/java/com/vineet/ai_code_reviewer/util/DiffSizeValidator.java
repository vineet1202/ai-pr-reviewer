package com.vineet.ai_code_reviewer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vineet.ai_code_reviewer.exception.DiffTooLargeException;

@Component
public class DiffSizeValidator {

    private final int maxCharacters;

    public DiffSizeValidator(@Value("${review.max-diff-characters:200000}") int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public void validate(String diff) {
        if (diff == null || diff.isBlank()) {
            throw new DiffTooLargeException("GitHub returned an empty pull request diff");
        }
        if (diff.length() > maxCharacters) {
            throw new DiffTooLargeException("Pull request diff exceeds the maximum supported size");
        }
    }
}
