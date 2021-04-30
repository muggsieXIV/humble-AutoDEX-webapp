package com.safelogic.autodex.web.validator;

import com.safelogic.autodex.web.BusinessValidationException;
import com.safelogic.autodex.web.transfer.objects.UserTO;

public class UserValidator implements Validator {

	@Override
	public void validate(Object inputData, int validateId) throws BusinessValidationException {
		StringBuilder messageBuilder = new StringBuilder();
		if (ValidationEnum.SAVE.getVal() == validateId) {
			validateSaveUser(inputData, validateId, messageBuilder);
		} else if (ValidationEnum.UPDATE.getVal() == validateId) {
			validateUpdateUser(inputData, validateId, messageBuilder);
		} else if (ValidationEnum.GET.getVal() == validateId) {
			validateGetUser(inputData, validateId, messageBuilder);
		} else if (ValidationEnum.DELETE.getVal() == validateId) {
			validateDeleteUser(inputData, validateId, messageBuilder);
		}
		ValidationUtil.sendBusinessValidationException(messageBuilder);
	}

	private void validateSaveUser(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Create user request", inputData, validateId, messageBuilder);
		UserTO userTO = (UserTO) inputData;
		ValidationUtil.validateNullOrBlank(validateId, "First Name", userTO.getFirstName(), messageBuilder);
		ValidationUtil.validateNullOrBlank(validateId, "Last Name", userTO.getLastName(), messageBuilder);
		ValidationUtil.validateNullOrBlank(validateId, "Mobile", userTO.getAutoDexNum(), messageBuilder);
		ValidationUtil.validateNullOrBlank(validateId, "Password", userTO.getPassword(), messageBuilder);
	}

	private void validateUpdateUser(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Update user request", inputData, validateId, messageBuilder);
		UserTO userTO = (UserTO) inputData;
		ValidationUtil.validateNullObject("User id", userTO.getId(), validateId, messageBuilder);
		//ValidationUtil.validateNullObject("Auto dex num", userTO.getAutoDexNum(), validateId, messageBuilder);
	}

	private void validateGetUser(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Get user request", inputData, validateId, messageBuilder);
		UserTO userTO = (UserTO) inputData;
		ValidationUtil.validateNullObject("User id", userTO.getId(), validateId, messageBuilder);
	}

	private void validateDeleteUser(Object inputData, int validateId, StringBuilder messageBuilder) {
		ValidationUtil.validateNullObject("Delete user request", inputData, validateId, messageBuilder);
		UserTO userTO = (UserTO) inputData;
		ValidationUtil.validateNullObject("User id", userTO.getId(), validateId, messageBuilder);
	}
}
