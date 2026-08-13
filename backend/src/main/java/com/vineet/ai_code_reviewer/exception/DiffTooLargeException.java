package com.vineet.ai_code_reviewer.exception;

public class DiffTooLargeException extends ReviewException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DiffTooLargeException(String message) { super(message); }
}