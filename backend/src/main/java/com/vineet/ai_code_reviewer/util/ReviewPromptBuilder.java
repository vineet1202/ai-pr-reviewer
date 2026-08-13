package com.vineet.ai_code_reviewer.util;

import org.springframework.stereotype.Component;

@Component
public class ReviewPromptBuilder {

    public String build(String diff) {
        return """
            You are a senior software engineer reviewing a GitHub pull request diff.
            Analyze ONLY the changed lines below (diff-only review — do not assume access to the full file).

            For each issue found, report:
            - filePath: the file it's in
            - diffPosition: the line-in-diff number where it occurs (omit if the issue is file-level, not line-specific)
            - severity: HIGH, MEDIUM, or LOW
            - category: BUG, SECURITY, STYLE, PERFORMANCE, or MAINTAINABILITY
            - message: a concise, specific explanation (1-2 sentences)

            Only report genuine issues. Do not invent problems to pad the list.

            DIFF:
            %s
            """.formatted(diff);
    }
}
