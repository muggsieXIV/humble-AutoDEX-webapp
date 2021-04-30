package com.safelogic.autodex.web;

import java.util.List;

import org.springframework.validation.Errors;

import com.safelogic.autodex.web.validator.ErrorMessage;

public class BusinessValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5054774338495665831L;

	List<ErrorMessage> messages;

	public BusinessValidationException() {
		super();
	}

	public BusinessValidationException(Throwable t) {
		super(t);
	}
	
	public BusinessValidationException(String message) {
		super(message);
	}

	public BusinessValidationException(String message, Errors errors) {
		super(message);
	}


	 public List<ErrorMessage> getMessages() {
	 return messages;
	 }
	
	 public void setMessages(List<ErrorMessage> messages) {
	 this.messages = messages;
	 }
}

