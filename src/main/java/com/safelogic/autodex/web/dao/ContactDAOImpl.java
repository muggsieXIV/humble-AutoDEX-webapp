package com.safelogic.autodex.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.safelogic.autodex.web.model.Contact;

@Repository("contactDao")
public class ContactDAOImpl extends NaasRepositoryImpl<Contact>  implements ContactDAO {

	@Override
	public Boolean deleteContact(long userId, long contactId) throws Exception {
		Contact contact = em.find(Contact.class, contactId);

		if (contact == null) {
			throw new Exception("invalid data passed in delete customer gorup service.");
		}
		if (userId != contact.getUser().getId()) {
			throw new Exception(
					"User id is different in database versus passed from UI for given contactId: " + contactId);
		}
		em.remove(contact);
		return Boolean.TRUE;
	}
}
