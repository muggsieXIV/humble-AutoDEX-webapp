package com.safelogic.autodex.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.safelogic.autodex.web.listener.NaasEntityListener;

@MappedSuperclass
@EntityListeners(NaasEntityListener.class)
public abstract class NaasEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3809711836141335907L;

	@Id
	@GeneratedValue
	protected long id;
	
	@Column(name="createUser")
	protected String createdByUser;
	
	@Column(name="updateUser")
	protected String modifiedByUser;
	
	@Column(name="createDate")
	//@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm a z")
	protected Date createDate;
	
	@Column(name="updateDate")
	//@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm a z")
	protected Date modifiedDate;
	
	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public String getModifiedByUser() {
		return modifiedByUser;
	}

	public void setModifiedByUser(String modifiedByUser) {
		this.modifiedByUser = modifiedByUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

		
	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Entity Class: ");
		builder.append(getClass().getName())
		.append(" Entity Name: ")
		//.append(getName())
		.append(" Entity Id: ")
		.append(getId());
		return builder.toString();
	}
}
