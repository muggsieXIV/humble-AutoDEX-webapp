package com.safelogic.autodex.web.validator;

import org.apache.commons.lang3.StringUtils;

import com.safelogic.autodex.web.BusinessValidationException;

public class ValidationUtil {

	public static void validateNullObject(String objectName,Object requestObj, int validateId, StringBuilder messageBuilder)
			throws BusinessValidationException {
		if (null == requestObj) {
			addMessage(messageBuilder, objectName+" is  null or blank");
			sendBusinessValidationException(messageBuilder);
		}
	}
	
	public static void validateBooleanObject(String objectName,boolean value,boolean matchValue, int validateId, StringBuilder messageBuilder)
			throws BusinessValidationException {
		if (value !=matchValue) {
			addMessage(messageBuilder, "User not active.");
			sendBusinessValidationException(messageBuilder);
		}
	}
	
	public static void validateNullObject(String objectName,Object requestObj, String inputId, int validateId, StringBuilder messageBuilder)
			throws BusinessValidationException {
		if (null == requestObj) {
			addMessage(messageBuilder, objectName+" is  null or blank for "+inputId);
			sendBusinessValidationException(messageBuilder);
		}
	}
	

	private static void addMessage(StringBuilder messageBuilder, String message) {
		if (messageBuilder.length() != 0) {
			messageBuilder.append(" | " + message);
		} else {
			messageBuilder.append(message);
		}
	}

	public static void validateNullOrBlank(int validateId, String fieldName, String fieldValue,	StringBuilder messageBuilder) {
		if (StringUtils.isBlank(fieldValue)) {
			addMessage(messageBuilder, fieldName + " can't be blank");
		}
	}

	public static void validateMinSize(int validateId, String fieldName, int minSize,int fieldValue,StringBuilder messageBuilder) {
		if (fieldValue <minSize) {
			addMessage(messageBuilder, fieldName + " must be greater or equal to "+minSize);
		}
	}
	
	public static void validateRangeInSize(int validateId, String fieldName, int minSize,int maxSize,int fieldValue,StringBuilder messageBuilder) {
		if ( ! (fieldValue >=minSize && fieldValue <=maxSize )) {
			addMessage(messageBuilder, fieldName + " must be greater or equal to "+minSize +" and less than or equal to "+maxSize);
		}
	}
	public static void validateMaxSize(int validateId, String fieldName, int maxSize,int fieldValue,StringBuilder messageBuilder) {
		if (fieldValue >maxSize) {
			addMessage(messageBuilder, fieldName + " must be less than "+maxSize);
		}
	}

	public static void validateEmail(int validateId, String fieldName, String fieldValue, StringBuilder messageBuilder) {
		if (fieldValue.indexOf("@") <= 0 || fieldValue.indexOf(".") <= 0) {
			addMessage(messageBuilder, fieldName + " is invalid. Please provide valid email.");
		}
	}
	
	public static void sendBusinessValidationException(StringBuilder errorMessageBuilder) throws BusinessValidationException {
		if (errorMessageBuilder.length() > 0) {
			throw new BusinessValidationException(errorMessageBuilder.toString());
		}
	}
	
}
