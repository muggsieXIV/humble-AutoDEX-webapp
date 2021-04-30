package com.safelogic.autodex.web.transfer.objects;

public class Login {
	
	private String autoDexNum;
	private String emailId = null;
	private String password = null;
	public String getAutoDexNum() {
		return autoDexNum;
	}
	public void setAutoDexNum(String autoDexNum) {
		this.autoDexNum = autoDexNum;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
