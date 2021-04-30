package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "UserDevice")
@NamedQuery(name = "UserDevice.findAll", query = "SELECT u FROM UserDevice u")
public class UserDevice extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7502156384168106498L;

	@Column(name = "deviceId")
	private String deviceId;

	@Column(name = "deviceType")
	private String deviceType;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}
