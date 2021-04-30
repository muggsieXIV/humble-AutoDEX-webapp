package com.safelogic.autodex.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "password_recovery")
@NamedQuery(name = "PasswordRecovery.findAll", query = "SELECT p FROM PasswordRecovery p")
public class PasswordRecovery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8523657205268854305L;

	public PasswordRecovery(){
		
	}
	
	@Id
	@GeneratedValue
	protected long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="retryCount")
	private int retryCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}


	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

}
