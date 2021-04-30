/**
 * 
 */
package com.safelogic.autodex.web.service;

import java.util.List;

import com.safelogic.autodex.web.model.Contact;
import com.safelogic.autodex.web.model.ContactBasicInfo;

public interface ContactsRestService {

	public List<Contact> getAll(long userId);

	public Contact getContact(long userId,long contactId);
	
	public Contact createContact(Contact contact);
	
	public Contact updateContact(Contact contact);
	
	public Boolean deleteContact(long userId, long contactId) throws Exception;
	
	public List<Contact> saveAllContact(List<Contact> contactList);
	
	public Contact saveOrUpdateContact(Contact contact, long contactId);
}
