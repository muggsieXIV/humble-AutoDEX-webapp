package com.safelogic.autodex.web.controller;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.safelogic.autodex.web.BusinessValidationException;


@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal server error occured")
	@ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

		logger.error("Error processing req: "+req.getRequestURL(), e);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("exp/global");
        return mav;
      //returning 500 error code
    }
	
	@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="business validation failed.")
	@ExceptionHandler(value = BusinessValidationException.class)
    public ModelAndView validationErrorHandler(HttpServletRequest req,HttpServletResponse res, BusinessValidationException e) throws BusinessValidationException {

		logger.error("Error processing req: "+req.getRequestURL(), e);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("errorMessages", e.getMessage());
        mav.setViewName("exp/global");
		res.addHeader("Validation-Message", e.getMessage());
		res.setStatus(422);
		return mav;
      //returning 422 error code for business validations.
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Bad request missing mandatory parameters.")
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public void handleParamMissingRqExp(){
		logger.error("Bad request exception while passing mandatory params.");
		//returning 400 error code
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="json parsing failed due to the bad request.")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void handleBadRequestException(){
		logger.error("json request parsing failed due to bad request");
		//returning 400 error code
	}
	
	@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="Service unavailable temporarily. ")
	@ExceptionHandler(ServiceUnavailableException.class)
	public void handleServiceunavailableException(){
		logger.error("Service unavailable temporarily.");
		//returning 503 error code
	}
}
