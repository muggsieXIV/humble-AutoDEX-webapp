package com.safelogic.autodex.web.security;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.safelogic.autodex.web.NaasURIConstants;

public class NaasCompanyCodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static Logger logger = LoggerFactory.getLogger(NaasCompanyCodeAuthenticationFilter.class);
	
	public NaasCompanyCodeAuthenticationFilter() {
		
	}
	
    @Override
    protected String obtainUsername(HttpServletRequest request)
    {
        String userName = request.getParameter(NaasURIConstants.userNameParamName);
        String companyCode = request.getParameter(NaasURIConstants.companyCodeParamName);
        String combinedUsername = userName + ":" + companyCode;
        logger.info("Combined username:{} " , combinedUsername);
        return combinedUsername;
    }
    

}
