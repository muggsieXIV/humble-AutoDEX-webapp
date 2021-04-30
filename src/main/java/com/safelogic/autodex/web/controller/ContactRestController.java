package com.safelogic.autodex.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.safelogic.autodex.web.NaasURIConstants;
import com.safelogic.autodex.web.model.Contact;
import com.safelogic.autodex.web.model.ContactAttribute;
import com.safelogic.autodex.web.model.ContactCategory;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.service.ContactsRestService;
import com.safelogic.autodex.web.util.NaasRestUtil;
import com.safelogic.autodex.web.validator.ContactValidator;
import com.safelogic.autodex.web.validator.ValidationEnum;

@RestController
@RequestMapping(value=NaasURIConstants.contactsBase)
public class ContactRestController {
	
	private Logger logger = LoggerFactory.getLogger(ContactRestController.class);
	ContactValidator validator=new ContactValidator();
	
	@Autowired
	private ContactsRestService contactService;
	
	private NaasRestUtil naasRestUtil;
	
	@Autowired
	@Qualifier("naasRestUtil")
	public void setNaasRestUtil(NaasRestUtil naasRestUtil) {
		this.naasRestUtil = naasRestUtil;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET ,produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Contact> getAllContacts(){
		Long userId=naasRestUtil.getUserId();
		logger.debug("getAllContacts request with userId = "+userId);
		List<Contact> contactList = contactService.getAll(userId);
		return contactList;
	}
	
	@RequestMapping(value="/{contactId}",method=RequestMethod.GET ,produces = {MediaType.APPLICATION_JSON_VALUE})
	public Contact getContact(@PathVariable(value="contactId") Long contactId){
		validator.validate(contactId, ValidationEnum.GET.getVal());
		Long userId=naasRestUtil.getUserId();
		logger.debug("getContact request with userId = "+userId+" , contactId = "+contactId);
		Contact contact = contactService.getContact(userId, contactId);
		User user=new User();
		user.setId(userId);
		contact.setUser(user);
		return contact;
	}
	
	@RequestMapping(method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Contact createContact(@RequestBody Contact contactRequest)  {
		validator.validate(contactRequest, ValidationEnum.SAVE.getVal());
		Long userId=naasRestUtil.getUserId();
		Contact contact = null;
		try {
			logger.debug("createContact request with userId = "+userId+" , contactName = "+contactRequest.getFirstName()+" ,attribute size = "+contactRequest.getContactAttributes().size());
			
			setAllContactAttributes(contactRequest);
			Contact dbContact=contactService.createContact(contactRequest);
			//logger.debug("Contact "+response.getName()+" ,Saved with Id = " + response.getId());
			contact = dbContact;

		} catch (Exception exp) {
			logger.error("Error happened in Create Contact for userId = " + userId, exp);
			System.out.println("Error happened in Create Contact for userId = " + userId+" "+ exp);
			/*throw new Exception(
					"Error happened in Create Contact for userId = " + userId + " , Please contact support.");
*/		}
		
		return contact;
	}
	
	@RequestMapping(value = "/saveAllContacts" ,method=RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long saveAllContacts(@RequestBody List<Contact> contactRequests) {
		validator.validate(contactRequests, 5);
		Long userId = naasRestUtil.getUserId();
		Long contactId = 0L;
		try {
			logger.debug("saveAllContacts request with userId = " + userId + " , contactSize = " + contactRequests.size());
			contactService.saveAllContact(contactRequests);
			/*for (Contact contactReq : contactRequests) {
				setAllContactAttributes(contactReq);
				Contact dbContact = contactService.saveOrUpdateContact(contactReq, contactReq.getId());
			}
*/
		} catch (Exception exp) {
			logger.error("Error happened in saveAllContacts for userId = " + userId, exp);
			System.out.println("Error happened in saveAllContacts for userId = " + userId + " " + exp);
		}
		return contactId;
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
	
	@RequestMapping(method=RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Contact updateContact(@RequestBody Contact contactRequest)  {
		
		Long userId=naasRestUtil.getUserId();
		logger.debug("updateContact request with userId = "+userId+" , contactName = "+contactRequest.getFirstName());
		validator.validate(contactRequest, ValidationEnum.UPDATE.getVal());
		Contact contact = null;
		try {
			logger.debug("createContact request with userId = "+userId+" , contactName = "+contactRequest.getFirstName()+" ,attribute size = "+contactRequest.getContactAttributes().size());
			Contact contactDB=contactService.getContact(userId, contactRequest.getId());
			validator.validate(contactRequest,2);//check for not null contact for the contactId
			setAllContactAttributes(contactRequest);
			contactRequest.setCreatedByUser(contactDB.getCreatedByUser());
			contactRequest.setCreateDate(contactDB.getCreateDate());
			//Update back to DB
			Contact dbContact=contactService.updateContact(contactRequest);
			//logger.debug("Contact "+response.getName()+" ,Saved with Id = " + response.getId());
			contact = dbContact;

		} catch (Exception exp) {
			logger.error("Error happened in update Contact for userId = " + userId+" , contactId= "+contact.getId(), exp);
			//System.out.println("Error happened in update Contact for userId = " + userId+" "+ exp);
			/*throw new Exception(
					"Error happened in update Contact for userId = " + userId + " , Please contact support.");
*/		}
		return contact;
	}
	
	@RequestMapping(value = "/profileImage/{contactId}", method = RequestMethod.GET )
	public void getProfileImage(@PathVariable(value="contactId") Long contactId,HttpServletResponse response) throws Exception {
		Long userId=naasRestUtil.getUserId();
		logger.debug("getProfileImage request with userId = "+userId+" , contactId = "+contactId);
		Contact dbContact = contactService.getContact(userId, contactId);
		validator.validate(dbContact, 2);// check for not null contact for the
		byte[] imageInBytes =dbContact.getProfileImage();
		//System.out.println("Image Byte Size"+imageInBytes.length);
		naasRestUtil.writeImageInBytesToResponse(response, imageInBytes);
	}
	
	@RequestMapping(value = "/updateProfileImage/{contactId}", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody boolean updateProfileImage(@PathVariable(value = "contactId") Long contactId,
			@RequestParam("file") MultipartFile file) {
		Long userId = naasRestUtil.getUserId();
		logger.debug("updateProfileImage request with userId = " + userId + " , contactId = " + contactId);
		Contact dbContact = contactService.getContact(userId, contactId);
		validator.validate(dbContact, 2);// check for not null contact for the
											// contactId
		byte[] fileBytes = naasRestUtil.convertImageToByteArray(file);
		dbContact.setProfileImage(fileBytes);
		contactService.updateContact(dbContact);
		return true;
	}
	
	@RequestMapping(value = "/{contactId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteContact(@PathVariable(value = "contactId") Long contactId) {

		Boolean bSuccess = Boolean.FALSE;
		Long userId = naasRestUtil.getUserId();
		logger.debug("deleteContact request with userId = " + userId + " , contactId = " + contactId);
		try {
			bSuccess = contactService.deleteContact(userId, contactId);
		} catch (Exception exp) {
			logger.error("Error happened in Delete Contact for userId = " + userId+" , contactId="+contactId, exp);
		}
		return bSuccess;
	}
	
}
