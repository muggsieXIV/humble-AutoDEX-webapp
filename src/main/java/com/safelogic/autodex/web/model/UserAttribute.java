package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "UserAttributes")
@NamedQuery(name = "UserAttribute.findAll", query = "SELECT u FROM UserAttribute u")
public class UserAttribute extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7502151384168105186L;

	@Column(name = "value")
	private String value;

	@Column(name = "tag")
	private Integer tag;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	@Column(name="name")
	private String name;


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
