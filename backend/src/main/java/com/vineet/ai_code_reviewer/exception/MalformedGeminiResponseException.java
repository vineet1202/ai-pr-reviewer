package com.vineet.ai_code_reviewer.exception;

public class MalformedGeminiResponseException extends UpstreamServiceException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MalformedGeminiResponseException(String message, Throwable cause) { super(message); initCause(cause); }
}