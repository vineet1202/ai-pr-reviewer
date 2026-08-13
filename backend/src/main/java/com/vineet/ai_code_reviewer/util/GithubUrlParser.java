package com.vineet.ai_code_reviewer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.vineet.ai_code_reviewer.dto.ParsedPrUrl;
import com.vineet.ai_code_reviewer.exception.InvalidPrUrlException;

@Component
public class GithubUrlParser {

    private static final Pattern PR_URL_PATTERN =
        Pattern.compile("^https://github\\.com/([^/]+)/([^/]+)/pull/(\\d+)/?$");

    public ParsedPrUrl parse(String prUrl) {
        Matcher matcher = PR_URL_PATTERN.matcher(prUrl);
        if (!matcher.matches()) {
            throw new InvalidPrUrlException("Not a valid GitHub PR URL: " + prUrl);
        }
        return new ParsedPrUrl(
            matcher.group(1),           // owner
            matcher.group(2),           // repo
            Integer.parseInt(matcher.group(3)) // PR number
        );
    }
}
