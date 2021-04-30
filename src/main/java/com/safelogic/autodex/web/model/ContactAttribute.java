package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ContactAttributes")
@NamedQuery(name = "ContactAttribute.findAll", query = "SELECT c FROM ContactAttribute c")
public class ContactAttribute extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5886106233779783081L;
	
	@Column(name = "name")
	private String name;

	@Column(name = "value")
	private String value;

	// bi-directional many-to-one association to Contact
	@ManyToOne
	@JoinColumn(name = "contactId")
	private Contact contact;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonIgnore
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
