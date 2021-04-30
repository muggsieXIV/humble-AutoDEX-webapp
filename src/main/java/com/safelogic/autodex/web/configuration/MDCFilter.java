package com.safelogic.autodex.web.configuration;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Component;

import com.safelogic.autodex.web.NaasURIConstants;
import com.safelogic.autodex.web.security.NaasAccountHolderUser;


@Component
public class MDCFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(MDCFilter.class);
	
	@Resource(name="tokenServices")
	DefaultTokenServices tokenServices;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		NaasAccountHolderUser authenticatedUserDetails = null;
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestURI = httpRequest.getRequestURI();
			getUserIdFromHeader(httpRequest);
			
			authenticatedUserDetails = authenticateOAuth2Token(request);
			if (authenticatedUserDetails != null) {
				dispatchRequest(request, response, chain, authenticatedUserDetails);
			} else {
				buildHttpErrorResponse(response, HttpStatus.SC_UNAUTHORIZED, "Invalid Token sent in the request.");
			}
			
//			if (StringUtils.isNotBlank(userId)) {
//				dispatchRequest(request, response, chain, userId);
//			} else {
//				buildHttpErrorResponse(response, HttpStatus.SC_UNAUTHORIZED,
//						"Invalid user details sent in the request.");
//			}
		}catch (AuthenticationException authExp) {
			buildHttpErrorResponse(response, HttpStatus.SC_UNAUTHORIZED, authExp.getMessage());
		} catch (Exception e) {
			logger.error("Server Error in Spring Filter",e);
			buildHttpErrorResponse(response, HttpStatus.SC_UNAUTHORIZED, "Server Error");
		}

	}

	private void buildHttpSuccesResponse(ServletResponse response) throws IOException {
		HttpServletResponse httpResp = (HttpServletResponse) response;
		httpResp.setStatus(HttpStatus.SC_OK);
	}
	private void buildHttpErrorResponse(ServletResponse response, int errorCode,String errorMsg) throws IOException {
		HttpServletResponse httpResp = (HttpServletResponse) response;
		httpResp.setStatus(errorCode);
		httpResp.sendError(errorCode, errorMsg);
	}

	private void dispatchRequest(ServletRequest request, ServletResponse response, FilterChain chain,
			NaasAccountHolderUser authenticatedUserDetails) throws IOException, ServletException {

		MDC.put("userName", "");
		MDC.put("userId", String.valueOf(authenticatedUserDetails.getId()));
		try {
			chain.doFilter(request, response);
		} finally {
			if (null != authenticatedUserDetails) {
				MDC.remove("userName");
				MDC.remove("userId");
			}
		}

	}
	
	private String getUserIdFromHeader(ServletRequest request) {

		String userId = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			userId = httpRequest.getHeader("userId");
		}
		logger.debug("Rest service request received userId = "+userId);
		return userId;
	}

	private NaasAccountHolderUser authenticateOAuth2Token(ServletRequest request) {
		String authToken = null;
		NaasAccountHolderUser loginUserDetails = null;
		authToken = getOAuth2AccessToken(request);
		if (!StringUtils.isBlank(authToken)) {
			OAuth2Authentication oAth2Obj = tokenServices.loadAuthentication(authToken);
			if (oAth2Obj != null) {
				loginUserDetails = (NaasAccountHolderUser) oAth2Obj.getDetails();
				logger.info("Token Validated Successfully, Login User Details-" + loginUserDetails.getUsername());
			}

		}

		return loginUserDetails;
	}

	private Boolean logOutWithOAuth2Token(ServletRequest request) {
		String authToken = null;
		Boolean logOutSuccessfull = Boolean.FALSE;
		authToken = getOAuth2AccessToken(request);
		if (!StringUtils.isBlank(authToken)) {
			logOutSuccessfull=tokenServices.revokeToken(authToken);
			logger.info("Token Invalidated Successfully, Token Details-" + authToken+ ", revokeTokenSuccessfull= "+logOutSuccessfull);
		}
		return logOutSuccessfull;
	}
	private String getOAuth2AccessToken(ServletRequest request) {

		String authToken = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			authToken = httpRequest.getHeader("token");
		}
		return authToken;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
