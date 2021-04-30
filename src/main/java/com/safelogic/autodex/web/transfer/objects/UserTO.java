package com.safelogic.autodex.web.transfer.objects;

public class UserTO {

	private Long id;
	private String password;

	private String firstName;

	private int firstNameTag;

	private String middleName;

	private int middleNameTag;

	private String lastName;

	private int lastNameTag;

	private boolean active;

	private String imieNumber;

	private String dob;

	private int dobTag;

	private String company;

	private int companyTag;

	private String designation;

	private int designationTag;

	private String personalEmail;

	private int personalEmailTag;

	private String businessEmail;

	private int businessEmailTag;

	private String address1;

	private int address1Tag;

	private String address2;

	private int address2Tag;

	private String city;

	private int cityTag;

	private String state;

	private int stateTag;

	private String zip;

	private int zipTag;

	private boolean newUser;

	private boolean paidUser = false;

	private String homeLatitude;
	private String homeLongitude;
	private String roamingHomeLatitude;
	private String roamingHomeLongtitude;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPaidUser() {
		return paidUser;
	}

	public void setPaidUser(boolean paidUser) {
		this.paidUser = paidUser;
	}
	
}
