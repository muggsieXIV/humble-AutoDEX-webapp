package com.safelogic.autodex.web.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.safelogic.autodex.web.BusinessValidationException;
import com.safelogic.autodex.web.model.Contact;

public class ContactValidator implements Validator {

	@Override
	public void validate(Object inputData, int validateId) throws BusinessValidationException {

		StringBuilder messageBuilder = new StringBuilder();
		if (validateId == 1) {
			validateSaveContact(inputData, validateId, messageBuilder);
		}
		if (validateId == 5) {
			validateSaveAllContacts(inputData, validateId, messageBuilder);
		}
		if (ValidationEnum.SAVE.getVal() == validateId) {
			validateSaveContact(inputData, validateId, messageBuilder);
		} else if (ValidationEnum.UPDATE.getVal() == validateId) {
			validateUpdateContact(inputData, validateId, messageBuilder);
		} else if (ValidationEnum.GET.getVal() == validateId) {
			validateGetContact(inputData, validateId, messageBuilder);
		} else if (ValidationEnum.DELETE.getVal() == validateId) {
			validateDeleteContact(inputData, validateId, messageBuilder);
		}
		ValidationUtil.sendBusinessValidationException(messageBuilder);
	}

	private void validateSaveContact(Object inputData, int validateId, StringBuilder messageBuilder)
			throws BusinessValidationException {
		validateContactRequest(inputData, validateId, messageBuilder);
	}
	
	@SuppressWarnings("unchecked")
	private void validateSaveAllContacts(Object inputData, int validateId, StringBuilder messageBuilder)
			throws BusinessValidationException {
		List<Contact> contactRequests = (List<Contact>) inputData;
		
		for(Contact contact: contactRequests) {
			validateContactRequest(contact, validateId, messageBuilder);
		}
	}

	private void validateContactRequest(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Create contact request", inputData, validateId, messageBuilder);
		Contact contact = (Contact) inputData;
		//ValidationUtil.validateNullOrBlank(validateId, "First Name", contact.getFirstName(), messageBuilder);
		//ValidationUtil.validateNullOrBlank(validateId, "Last Name", contact.getLastName(), messageBuilder);
		if(StringUtils.isBlank(contact.getFirstName()) && StringUtils.isBlank(contact.getLastName())) {
			throw new BusinessValidationException("Both First Name and Last Name cannot be blank");
		}
	}
	private void validateUpdateContact(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Create contact request", inputData, validateId, messageBuilder);
		Contact contact = (Contact) inputData;
		ValidationUtil.validateNullObject("Contact Id", contact.getId(), validateId, messageBuilder);
//		ValidationUtil.validateNullOrBlank(validateId, "First Name", contact.getFirstName(), messageBuilder);
	}
	
	private void validateGetContact(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Contact Id", inputData, validateId, messageBuilder);
	}
	private void validateDeleteContact(Object inputData, int validateId, StringBuilder messageBuilder) {
		
	}
	
	

}
