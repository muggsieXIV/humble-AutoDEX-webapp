package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "UserProfileImage")
@NamedQuery(name="UserProfileImage.findAll", query="SELECT u FROM UserProfileImage u")
public class UserProfileImage extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7167145729283597997L;

	public UserProfileImage() {
	};

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@Lob
	@Column(name = "image")
	private byte[] profilePic;

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
