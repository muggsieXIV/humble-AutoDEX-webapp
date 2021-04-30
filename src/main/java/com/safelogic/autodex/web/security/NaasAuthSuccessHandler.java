package com.safelogic.autodex.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("naasAuthSuccessHandler")
public class NaasAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("fileLogger");
	
	/*@Autowired 
	private AccountHolderUserService accountHolderUserService;
	
	@Autowired 
	private AccountHolderService accountHolderService;
	
	@Autowired
	private NotificationService notificationService;
	*/
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if(authentication.getPrincipal()  instanceof NaasAccountHolderUser){
			/*NaasAccountHolderUser user = (NaasAccountHolderUser)authentication.getPrincipal();
			logger.debug("Logged in user: "+user.getUsername());
			AccountHolder accountHolder = accountHolderService.getAccountHolder(user.getAccountHolderName());
			AccountHolderUser currentUser = accountHolderUserService.getActiveAccoutHolderUser(user.getUsername(),accountHolder);
			HttpSession session = request.getSession();
			if(session.getAttribute("loggedInUser") == null){
				session.setAttribute("loggedInUser", currentUser.getDisplayName());
			}
			
			if(session.getAttribute("accountHolderName") == null){
				session.setAttribute("accountHolderName", accountHolder.getName());
			}
			
			
			session.setAttribute("accountHolder", accountHolder);
			session.setAttribute("accountHolderUser", currentUser);
			
			String monthlyEmailCount = "" + notificationService.getMonthlyDeliveredEmailCount(accountHolder);			
			String monthlySmsCount = "" + notificationService.getMonthlyDeliveredSmsCount(accountHolder);
			
			logger.debug("monthlyEmailCount: " + monthlyEmailCount);
			logger.debug("monthlySmsCount: " + monthlySmsCount);
			
			session.setAttribute("monthlyEmailCount", monthlyEmailCount);			
			session.setAttribute("monthlySmsCount", monthlySmsCount);*/
			
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
