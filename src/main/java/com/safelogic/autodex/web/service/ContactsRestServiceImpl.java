/**
 * 
 */
package com.safelogic.autodex.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safelogic.autodex.web.dao.ContactDAO;
import com.safelogic.autodex.web.dao.NaasRepository;
import com.safelogic.autodex.web.model.Contact;
import com.safelogic.autodex.web.model.ContactAttribute;
import com.safelogic.autodex.web.model.ContactBasicInfo;
import com.safelogic.autodex.web.model.ContactCategory;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.util.NaasRestUtil;

@Service
@Transactional
public class ContactsRestServiceImpl implements ContactsRestService {
	
	private NaasRepository<Contact> contactRepo;
	private NaasRepository<ContactBasicInfo> contactBasicInfoRepo;
	
	private ContactDAO contactDao;
	private NaasRestUtil naasRestUtil;

	@Autowired
	@Qualifier("contactDao")
	public void setContactDao(ContactDAO contactDao) {
		this.contactDao = contactDao;
	}
	@Autowired
	@Qualifier("naasRepository")
	public void setContactRepo(NaasRepository<Contact> contactRepo) {
		this.contactRepo = contactRepo;
		this.contactRepo.setType(Contact.class);
	}
	@Autowired
	@Qualifier("naasRepository")
	public void setContactBasicInfoRepo(NaasRepository<ContactBasicInfo> contactBasicRepo) {
		this.contactBasicInfoRepo = contactBasicRepo;
		this.contactBasicInfoRepo.setType(ContactBasicInfo.class);
	}
	
	@Autowired
	@Qualifier("naasRestUtil")
	public void setNaasRestUtil(NaasRestUtil naasRestUtil) {
		this.naasRestUtil = naasRestUtil;
	}

	@Override
	public List<Contact> getAll(long userId) {
		return contactRepo.findAllByUserId(userId);
	}

	@Override
	public Contact getContact(long userId,long contactId) {
		return contactRepo.findById(contactId,userId);
	}

	@Override
	public Contact createContact(Contact contact) {
		return contactRepo.create(contact);
	}

	@Override
	public Contact updateContact(Contact contact) {
		return contactRepo.update(contact);
	}

	@Override
	public Boolean deleteContact(long userId,long contactId) throws Exception {
		return contactDao.deleteContact(userId, contactId);
	}
	@Override
	public Contact saveOrUpdateContact(Contact contact, long contactId) {
		return contactRepo.save(contact, contactId);
	}
	@Transactional
	public List<Contact> saveAllContact(List<Contact> contacts) {
		List<Contact> result = new ArrayList<Contact>();

		for (Contact contact : contacts) {
			if(!duplicateContact(result, contact)) {
				setAllContactAttributes(contact);
				result.add(saveOrUpdateContact(contact, contact.getId()));
			}
		}
		System.out.println("saveOrUpdateCompleted");
		return result;
	}
	
	private boolean duplicateContact(List<Contact> contactList, Contact contact) {
		
		if(contact == null || contact.getId() <= 0) {
			return false;
		}
		
		for (Contact existingContact: contactList) {
			if(existingContact.getId() > 0L && existingContact.getId() == contact.getId()){
				return true;
			}
		}
		
		return false;
	}
	
	private void setAllContactAttributes(Contact contactRequest) {
		
		Long userId=naasRestUtil.getUserId();
		User user= new User();
		user.setId(userId);
		contactRequest.setUser(user);
		
		for(ContactAttribute cattr:contactRequest.getContactAttributes()){
			cattr.setContact(contactRequest);
		}
		
		for(ContactCategory categ:contactRequest.getContactCategories()){
			categ.setContact(contactRequest);
		}
	}
}
