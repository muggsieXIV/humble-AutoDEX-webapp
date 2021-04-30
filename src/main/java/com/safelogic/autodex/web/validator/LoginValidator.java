package com.safelogic.autodex.web.validator;

import com.safelogic.autodex.web.BusinessValidationException;
import com.safelogic.autodex.web.transfer.objects.Login;

public class LoginValidator implements Validator{

	@Override
	public void validate(Object inputData, int validateId) throws BusinessValidationException {
		StringBuilder messageBuilder = new StringBuilder();
		if (validateId == 1) {
			validateActionUser(inputData, validateId, messageBuilder);
		}
		ValidationUtil.sendBusinessValidationException(messageBuilder);
	}

	private void validateActionUser(Object inputData, int validateId, StringBuilder messageBuilder)
			throws BusinessValidationException {
		ValidationUtil.validateNullObject("Login Request ",inputData, validateId, messageBuilder);
		Login loginUserProfile=(Login)inputData;
		ValidationUtil.validateNullOrBlank(validateId,"Mobile Number", loginUserProfile.getAutoDexNum(), messageBuilder);
		ValidationUtil.validateNullOrBlank(validateId, "Password", loginUserProfile.getPassword(), messageBuilder);
	}

}
