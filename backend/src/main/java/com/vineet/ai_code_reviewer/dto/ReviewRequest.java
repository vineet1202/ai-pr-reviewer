package com.vineet.ai_code_reviewer.dto;

public class ReviewRequest {
	 private String code;
	 private String language; 
	 public String getCode() {
		 return code;
	 }
	 public void setCode(String code) {
		 this.code = code;
	 }
	 public String getLanguage() {
		 return language;
	 }
	 public void setLanguage(String language) {
		 this.language = language;
	 }
	 public ReviewRequest(String code, String language) {
		this.code = code;
		this.language = language;
	 }

	 public ReviewRequest() {
	 }
	 
	 
}
