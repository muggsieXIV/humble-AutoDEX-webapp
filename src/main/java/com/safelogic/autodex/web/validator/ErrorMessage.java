package com.safelogic.autodex.web.validator;

import java.io.Serializable;
import java.util.List;

public class ErrorMessage implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3485410488948711385L;
	
	public ErrorMessage(){
		
	}
	
	public ErrorMessage(int messageCode, List<String> messages){
		this.messageCode = messageCode;
		this.messages = messages;
	}
	
	int messageCode;
	List<String> messages;

	public int getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	
}
