package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UserPreference")
@NamedQuery(name="UserPreference.findAll", query="SELECT u FROM UserPreference u")
public class UserPreference extends  NaasEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7171658553033603963L;

	public UserPreference() {
	};

	@JoinColumn(name = "userId")
	private Long userId;

	@Column(name = "contactNearMeNotify")
	private boolean contactNearMeNotify;

	@Column(name = "birthdayNotify")
	private boolean birthdayNotify;

	@Column(name = "newAutoDexUserNotify")
	private boolean newAutoDexUserNotify;

	@Column(name = "showNotificationAlert")
	private boolean showNotificationAlert;

	@Column(name = "vibrateOnNotificationAlert")
	private boolean vibrateOnNotificationAlert;

	@Column(name = "popUpNotify")
	private boolean popUpNotify;

	@Column(name = "changeNumWithCountryCode")
	private boolean changeNumWithCountryCode;

	@Column(name = "enableOtpNotification")
	private boolean enableOtpNotification;

	@Column(name = "manageBlockUnBlockContacts")
	private boolean manageBlockUnBlockContacts;

	@Column(name = "manageUserInfo")
	private boolean manageUserInfo;

	@Column(name = "showFirstLastNameWithImage")
	private boolean showFirstLastNameWithImage;

	@Column(name = "notifyMiles")
	private long notifyMiles;

	public boolean isContactNearMeNotify() {
		return contactNearMeNotify;
	}

	public void setContactNearMeNotify(boolean contactNearMeNotify) {
		this.contactNearMeNotify = contactNearMeNotify;
	}

	public boolean isBirthdayNotify() {
		return birthdayNotify;
	}

	public void setBirthdayNotify(boolean birthdayNotify) {
		this.birthdayNotify = birthdayNotify;
	}

	public boolean isNewAutoDexUserNotify() {
		return newAutoDexUserNotify;
	}

	public void setNewAutoDexUserNotify(boolean newAutoDexUserNotify) {
		this.newAutoDexUserNotify = newAutoDexUserNotify;
	}

	public boolean isShowNotificationAlert() {
		return showNotificationAlert;
	}

	public void setShowNotificationAlert(boolean showNotificationAlert) {
		this.showNotificationAlert = showNotificationAlert;
	}

	public boolean isVibrateOnNotificationAlert() {
		return vibrateOnNotificationAlert;
	}

	public void setVibrateOnNotificationAlert(boolean vibrateOnNotificationAlert) {
		this.vibrateOnNotificationAlert = vibrateOnNotificationAlert;
	}

	public boolean isPopUpNotify() {
		return popUpNotify;
	}

	public void setPopUpNotify(boolean popUpNotify) {
		this.popUpNotify = popUpNotify;
	}

	public boolean isChangeNumWithCountryCode() {
		return changeNumWithCountryCode;
	}

	public void setChangeNumWithCountryCode(boolean changeNumWithCountryCode) {
		this.changeNumWithCountryCode = changeNumWithCountryCode;
	}

	public boolean isEnableOtpNotification() {
		return enableOtpNotification;
	}

	public void setEnableOtpNotification(boolean enableOtpNotification) {
		this.enableOtpNotification = enableOtpNotification;
	}

	public boolean isManageBlockUnBlockContacts() {
		return manageBlockUnBlockContacts;
	}

	public void setManageBlockUnBlockContacts(boolean manageBlockUnBlockContacts) {
		this.manageBlockUnBlockContacts = manageBlockUnBlockContacts;
	}

	public boolean isManageUserInfo() {
		return manageUserInfo;
	}

	public void setManageUserInfo(boolean manageUserInfo) {
		this.manageUserInfo = manageUserInfo;
	}

	public boolean isShowFirstLastNameWithImage() {
		return showFirstLastNameWithImage;
	}

	public void setShowFirstLastNameWithImage(boolean showFirstLastNameWithImage) {
		this.showFirstLastNameWithImage = showFirstLastNameWithImage;
	}

	public long getNotifyMiles() {
		return notifyMiles;
	}

	public void setNotifyMiles(long notifyMiles) {
		this.notifyMiles = notifyMiles;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
