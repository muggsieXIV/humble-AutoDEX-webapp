package com.safelogic.autodex.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class NaasLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	Logger fileLogger = LoggerFactory.getLogger("fileLogger");
		
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		System.out.println("onLogoutSuccess called"+authentication);
		if(authentication.getPrincipal()  instanceof NaasAccountHolderUser){
			NaasAccountHolderUser user = (NaasAccountHolderUser)authentication.getPrincipal();			
			fileLogger.debug("User logged out: "+user.getUsername());
		}
		super.onLogoutSuccess(request, response, authentication);
	}
}
