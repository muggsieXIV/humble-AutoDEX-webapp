package com.safelogic.autodex.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ContactCategory")
@NamedQuery(name="ContactCategory.findAll", query="SELECT c FROM ContactCategory c")
public class ContactCategory extends NaasEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3894009281317301770L;

	// bi-directional many-to-one association to Contact
	@ManyToOne
	@JoinColumn(name = "contactId")
	private Contact contact;

	@Column(name = "category")
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@JsonIgnore
	public Contact getContact() {
		return this.contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
