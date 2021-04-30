package com.safelogic.autodex.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NaasUserDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(NaasUserDetailsService.class);
	
	/*@Autowired
	@Qualifier("accountHolderUserService")
	private AccountHolderUserService accountHolderUserService;
	

	private AccountHolderDAO accountHolderDao;*/
	
	@Autowired
	MessageSourceAccessor messageSourceAccessor;
	
	public NaasUserDetailsService() {
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String combinedUserName) throws UsernameNotFoundException {

		try{
			
			/*logger.debug("User Name in NaasUserDetails Service: {}",combinedUserName);
			String userName = combinedUserName.split(":")[0];
			String accountName = combinedUserName.split(":")[1];
			AccountHolder accountHolder = new AccountHolder();//accountHolderDao.findByAttribute("name", accountName);
			accountHolder.setName(accountName);
			AccountHolderUser profile = accountHolderUserService.getAccountHolderUser(userName, accountHolder);
			if(!profile.isEnabled()){
				throw new UsernameNotFoundException(messageSourceAccessor.getMessage("NaasUserDetailsService.userIdDisabled",new Object[]{userName}));
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			for(Role role: profile.getRoles()){
				GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
				authorities.add(authority);
			}*/
			NaasAccountHolderUser userDetails = new NaasAccountHolderUser("", "", null);
			/*userDetails.setAccountHolderName(accountName);
			userDetails.setFirstTimeLogin(profile.isFirstTimeLogin());
			userDetails.setId(profile.getId());*/
			return userDetails;
		}catch(EmptyResultDataAccessException exp){
			throw new UsernameNotFoundException(messageSourceAccessor.getMessage("NaasUserDetailsService.userIdInvalid"));
		}
	}
	
	/*@Autowired
	@Qualifier("accountHolderDao")
	public void setAccountHolderDao(AccountHolderDAO naasAccountHolderRepository){
		this.accountHolderDao = naasAccountHolderRepository;
		this.accountHolderDao.setType(AccountHolder.class);
	}*/
	
}
