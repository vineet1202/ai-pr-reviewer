package com.vineet.ai_code_reviewer.exception;

//404-type
public class ReviewNotFoundException extends ReviewException {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 public ReviewNotFoundException(Long id) {
     super("Review not found: " + id);
 }
}
