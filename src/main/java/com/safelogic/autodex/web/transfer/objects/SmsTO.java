package com.safelogic.autodex.web.transfer.objects;

import java.io.Serializable;

public class SmsTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8609904444676221095L;
	
	public SmsTO(){
		
	}
	
	private String phoneNumber;
	private String textMessage;

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getTextMessage() {
		return textMessage;
	}
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	
}
