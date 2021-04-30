package com.safelogic.autodex.web.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Contacts")
@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c")
public class Contact extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8765648148234717130L;

	public Contact() {

	}

	private String profileTag;

	private String firstName;

	private String middleName;

	private String lastName;

	private long contactUserId;

	private Boolean favorite;

	private String dob;

	private String phoneNumber;

	private Boolean useEmail;

	private String address1;

	private String address2;

	private String city;

	private String state;

	private String zip;
	
	@Lob
	private byte[] profileImage;

	@OneToMany(mappedBy = "contact", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
	private List<ContactAttribute> contactAttributes;

	@OneToMany(mappedBy = "contact", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
	private List<ContactCategory> contactCategories;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public String getProfileTag() {
		return profileTag;
	}

	public void setProfileTag(String profileTag) {
		this.profileTag = profileTag;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(long contactUserId) {
		this.contactUserId = contactUserId;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getUseEmail() {
		return useEmail;
	}

	public void setUseEmail(Boolean useEmail) {
		this.useEmail = useEmail;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<ContactAttribute> getContactAttributes() {
		return this.contactAttributes;
	}

	public void setContactAttributes(List<ContactAttribute> contactAttributes) {
		this.contactAttributes = contactAttributes;
	}

	public ContactAttribute addContactAttribute(ContactAttribute contactAttribute) {
		getContactAttributes().add(contactAttribute);
		contactAttribute.setContact(this);

		return contactAttribute;
	}

	public ContactAttribute removeContactAttribute(ContactAttribute contactAttribute) {
		getContactAttributes().remove(contactAttribute);
		contactAttribute.setContact(null);

		return contactAttribute;
	}

	public List<ContactCategory> getContactCategories() {
		return this.contactCategories;
	}

	public void setContactCategories(List<ContactCategory> contactCategories) {
		this.contactCategories = contactCategories;
	}

	public ContactCategory addContactCategory(ContactCategory contactCategory) {
		getContactCategories().add(contactCategory);
		contactCategory.setContact(this);

		return contactCategory;
	}

	public ContactCategory removeContactCategory(ContactCategory contactCategory) {
		getContactCategories().remove(contactCategory);
		contactCategory.setContact(null);

		return contactCategory;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public byte[] getProfileImage() {
		byte[] image=new byte[0];
		if(null != profileImage){
			image=profileImage;
		}
		return image;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}
	
}
