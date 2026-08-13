package com.vineet.ai_code_reviewer.exception;

public abstract class ReviewException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ReviewException(String message) { super(message); }
}