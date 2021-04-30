package com.safelogic.autodex.web.validator;

import com.safelogic.autodex.web.BusinessValidationException;

public interface Validator {
	
	void validate (Object inputData, int validateId)throws BusinessValidationException;
}
