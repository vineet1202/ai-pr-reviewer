package com.vineet.ai_code_reviewer.exception;

//400-type — bad client input
public class InvalidPrUrlException extends ReviewException {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 public InvalidPrUrlException(String message) { super(message); }
}
