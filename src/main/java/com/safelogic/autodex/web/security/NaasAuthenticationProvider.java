package com.safelogic.autodex.web.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Qualifier("naasAuthProvider")
public class NaasAuthenticationProvider implements AuthenticationProvider {

	Logger logger = LoggerFactory.getLogger(NaasAuthenticationProvider.class);
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private MessageSourceAccessor messageSourceAccessor;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        logger.debug("username: {}",username);
        logger.debug("password: {}",password);
        
        UserDetails user = userDetailsService.loadUserByUsername(username);
 
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException(messageSourceAccessor.getMessage("NaasAuthenticationProvider.invalidPassword"));
        }
 
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
 
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
	

}
