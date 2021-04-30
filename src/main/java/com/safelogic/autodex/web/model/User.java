package com.safelogic.autodex.web.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "User")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3571494456877166131L;

	public User() {
	}

	@Column(name = "password")
	private String password;

	@Column(name = "firstName")
	private String firstName;

	@Column(name = "firstNameTag")
	private int firstNameTag;

	@Column(name = "middleName")
	private String middleName;

	@Column(name = "middleNameTag")
	private int middleNameTag;

	@Column(name = "lastName")
	private String lastName;

	@Column(name = "lastNameTag")
	private int lastNameTag;

	@Column(name = "active")
	private boolean active;

	@Column(name = "imieNumber")
	private String imieNumber;

	@Column(name = "dob")
	private String dob;

	@Column(name = "dobTag")
	private int dobTag;

	@Column(name = "company")
	private String company;

	@Column(name = "companyTag")
	private int companyTag;

	@Column(name = "designation")
	private String designation;

	@Column(name = "designationTag")
	private int designationTag;

	@Column(name = "personalEmail")
	private String personalEmail;

	@Column(name = "personalEmailTag")
	private int personalEmailTag;

	@Column(name = "businessEmail")
	private String businessEmail;

	@Column(name = "businessEmailTag")
	private int businessEmailTag;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address1Tag")
	private int address1Tag;

	@Column(name = "address2")
	private String address2;

	@Column(name = "address2Tag")
	private int address2Tag;

	@Column(name = "city")
	private String city;

	@Column(name = "cityTag")
	private int cityTag;

	@Column(name = "state")
	private String state;

	@Column(name = "stateTag")
	private int stateTag;

	@Column(name = "zip")
	private String zip;

	@Column(name = "zipTag")
	private int zipTag;

	@Column(name = "new")
	private boolean newUser;

	private String homeLatitude;
	private String homeLongitude;
	private String roamingHomeLatitude;
	private String roamingHomeLongtitude;

	// bi-directional many-to-one association to Contact
	@OneToMany(mappedBy = "user")
	private List<Contact> contacts;

	// bi-directional many-to-one association to UserAttribute
	@OneToMany(mappedBy = "user")
	private List<UserAttribute> userAttributes;

	// bi-directional many-to-one association to UserProfileImage
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL, CascadeType.PERSIST,
			CascadeType.MERGE }, mappedBy = "user")
	private List<UserProfileImage> userProfileImages;
	
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private PasswordRecovery pwdRecovery;

	private String autoDexNum;
	private int autoDexNumTag;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getFirstNameTag() {
		return firstNameTag;
	}

	public void setFirstNameTag(int firstNameTag) {
		this.firstNameTag = firstNameTag;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public int getMiddleNameTag() {
		return middleNameTag;
	}

	public void setMiddleNameTag(int middleNameTag) {
		this.middleNameTag = middleNameTag;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getLastNameTag() {
		return lastNameTag;
	}

	public void setLastNameTag(int lastNameTag) {
		this.lastNameTag = lastNameTag;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getImieNumber() {
		return imieNumber;
	}

	public void setImieNumber(String imieNumber) {
		this.imieNumber = imieNumber;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public int getDobTag() {
		return dobTag;
	}

	public void setDobTag(int dobTag) {
		this.dobTag = dobTag;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getCompanyTag() {
		return companyTag;
	}

	public void setCompanyTag(int companyTag) {
		this.companyTag = companyTag;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getDesignationTag() {
		return designationTag;
	}

	public void setDesignationTag(int designationTag) {
		this.designationTag = designationTag;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<UserAttribute> getUserAttributes() {
		return userAttributes;
	}

	public void setUserAttributes(List<UserAttribute> userAttributes) {
		this.userAttributes = userAttributes;
	}

	public List<UserProfileImage> getUserProfileImages() {
		return userProfileImages;
	}

	public void setUserProfileImages(List<UserProfileImage> userProfileImages) {
		this.userProfileImages = userProfileImages;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public int getPersonalEmailTag() {
		return personalEmailTag;
	}

	public void setPersonalEmailTag(int personalEmailTag) {
		this.personalEmailTag = personalEmailTag;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public int getBusinessEmailTag() {
		return businessEmailTag;
	}

	public void setBusinessEmailTag(int businessEmailTag) {
		this.businessEmailTag = businessEmailTag;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public int getAddress1Tag() {
		return address1Tag;
	}

	public void setAddress1Tag(int address1Tag) {
		this.address1Tag = address1Tag;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public int getAddress2Tag() {
		return address2Tag;
	}

	public void setAddress2Tag(int address2Tag) {
		this.address2Tag = address2Tag;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCityTag() {
		return cityTag;
	}

	public void setCityTag(int cityTag) {
		this.cityTag = cityTag;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getStateTag() {
		return stateTag;
	}

	public void setStateTag(int stateTag) {
		this.stateTag = stateTag;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public int getZipTag() {
		return zipTag;
	}

	public void setZipTag(int zipTag) {
		this.zipTag = zipTag;
	}

	public String getHomeLatitude() {
		return homeLatitude;
	}

	public void setHomeLatitude(String homeLatitude) {
		this.homeLatitude = homeLatitude;
	}

	public String getHomeLongitude() {
		return homeLongitude;
	}

	public void setHomeLongitude(String homeLongitude) {
		this.homeLongitude = homeLongitude;
	}

	public String getRoamingHomeLatitude() {
		return roamingHomeLatitude;
	}

	public void setRoamingHomeLatitude(String roamingHomeLatitude) {
		this.roamingHomeLatitude = roamingHomeLatitude;
	}

	public String getRoamingHomeLongtitude() {
		return roamingHomeLongtitude;
	}

	public void setRoamingHomeLongtitude(String roamingHomeLongtitude) {
		this.roamingHomeLongtitude = roamingHomeLongtitude;
	}

	public String getAutoDexNum() {
		return autoDexNum;
	}

	public void setAutoDexNum(String autoDexNum) {
		this.autoDexNum = autoDexNum;
	}

	public int getAutoDexNumTag() {
		return autoDexNumTag;
	}

	public void setAutoDexNumTag(int autoDexNumTag) {
		this.autoDexNumTag = autoDexNumTag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
}
