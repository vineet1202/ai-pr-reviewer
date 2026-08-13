package com.vineet.ai_code_reviewer.exception;

//502-type — upstream (GitHub/Gemini) failed, not our fault
public abstract class UpstreamServiceException extends ReviewException {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 protected UpstreamServiceException(String message) { super(message); }
}
