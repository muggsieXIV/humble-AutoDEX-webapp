package com.safelogic.autodex.web.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.safelogic.autodex.web.NaasURIConstants;
import com.safelogic.autodex.web.security.NaasAuthSuccessHandler;
import com.safelogic.autodex.web.security.NaasCompanyCodeAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class NaasSecuritySetupConfig extends WebSecurityConfigurerAdapter{
	
	
	private static final Logger logger = LoggerFactory.getLogger(NaasSecuritySetupConfig.class);
	
	@Autowired
	@Qualifier("naasAuthProvider")
	private AuthenticationProvider naasAuthProvider;
	
	@Autowired
	@Qualifier("naasUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;
	
    @Autowired
    private HttpAuthenticationEntryPoint authenticationEntryPoint;
    
    @Autowired
    private NaasAuthSuccessHandler naasAuthSuccessHandler;
    
    @Autowired
    private AuthFailureHandler authFailureHandler;
   
	@Autowired
	private DataSource dataSource;

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new ShaPasswordEncoder());

        return authenticationProvider;
    }
	
	@Autowired
	private MDCFilter mdcFilter;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, 
								AuthenticationProvider provider) throws Exception {

	    auth.authenticationProvider(daoAuthenticationProvider());
	    auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(mdcFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/rest").permitAll()
        .antMatchers(NaasURIConstants.contactsBase+"/**").permitAll()
        .antMatchers(NaasURIConstants.autodexAppUrl+"/**").access("hasRole('USER') OR hasRole('AUTHOR') OR hasRole('ADMIN')")
        .antMatchers(NaasURIConstants.userBase+"/**").access("hasRole('USER') OR hasRole('AUTHOR') OR hasRole('ADMIN')")
        
        .and()
        .authenticationProvider(authenticationProvider())
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        	.formLogin()
        	
        .and()
        	.logout().permitAll();
        
        //http.authorizeRequests().anyRequest().authenticated();
	}
    
	@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

   @Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean(){
    	try{
    		return super.authenticationManagerBean();
    	}catch(Exception exp){
    		exp.printStackTrace();
    		logger.error("Problem creating auth manager!!", exp);
    		throw new RuntimeException("Problem creating auth manager!!",exp);
    	}
    }
   
   @Bean
    public UsernamePasswordAuthenticationFilter getFilter(){
    	
    	UsernamePasswordAuthenticationFilter filter = new NaasCompanyCodeAuthenticationFilter();
    	filter.setAuthenticationManager(authenticationManagerBean());
    	filter.setFilterProcessesUrl(NaasURIConstants.loginProcessingUrl);
    	    	
    	authenticationSuccessHandler.setDefaultTargetUrl(NaasURIConstants.autodexAppUrl);
    	filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    	
   	
    	SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
    	failureHandler.setDefaultFailureUrl(NaasURIConstants.loginFailureUrl);
    	filter.setAuthenticationFailureHandler(failureHandler);
    	return filter;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
    	auth.authenticationProvider(daoAuthenticationProvider());
    	auth.authenticationProvider(naasAuthProvider);
    }
    
    @Override
   	public void configure(WebSecurity web) throws Exception {
   	    web
   	      .ignoring()
   	        .antMatchers(NaasURIConstants.login+"/**", 
   	        		NaasURIConstants.autodexUserBase+"/logout/**",
   	        		NaasURIConstants.autodexUserBase + "/createProfile/**");
   	}
	
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
 