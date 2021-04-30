package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "UserNotifications")
@NamedQuery(name = "UserNotification.findAll", query = "SELECT u FROM UserNotification u")
public class UserNotification extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3308229704143296433L;

	@JoinColumn(name = "userId")
	private Long userId;

	@Column(name = "contactId")
	private long contactId;

	@Column(name = "type")
	private String type;

	@Column(name = "message")
	private String message;
	
	@Column(name = "readFlag")
	private boolean read;

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
