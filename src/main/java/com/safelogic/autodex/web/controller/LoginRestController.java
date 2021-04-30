package com.safelogic.autodex.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.safelogic.autodex.web.NaasURIConstants;
import com.safelogic.autodex.web.configuration.NaasAppConfig;
import com.safelogic.autodex.web.model.BasicUserInfo;
import com.safelogic.autodex.web.model.PasswordRecovery;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.security.NaasAccountHolderUser;
import com.safelogic.autodex.web.service.UserService;
import com.safelogic.autodex.web.transfer.objects.Login;
import com.safelogic.autodex.web.transfer.objects.OTPValidator;
import com.safelogic.autodex.web.transfer.objects.UserTO;
import com.safelogic.autodex.web.util.NaasRestUtil;
import com.safelogic.autodex.web.util.OTPGeneratorUtil;
import com.safelogic.autodex.web.validator.LoginValidator;
import com.safelogic.autodex.web.validator.ValidationUtil;

@RestController
@RequestMapping(value = NaasURIConstants.login)
public class LoginRestController {

	private Logger logger = LoggerFactory.getLogger(LoginRestController.class);
	LoginValidator validator = new LoginValidator();
	private static int FIFTEEN_MINUTES = 15;
	private NaasRestUtil naasRestUtil;

	@Autowired
	@Qualifier("naasRestUtil")
	public void setNaasRestUtil(NaasRestUtil naasRestUtil) {
		this.naasRestUtil = naasRestUtil;
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Resource(name = "tokenServices")
	AuthorizationServerTokenServices tokenServices;

	public static final String GRANT_TYPE = "grant_type";
	public static final String PASSWORD = "password";
	public static final String CLIENT_CREDENTIALS = "client_credentials";

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> loginUser(@RequestBody Login loginRequest) {

		ResponseEntity<Object> response = null;
		validator.validate(loginRequest, 1);
		
		UserTO loginUserProfile = userService.getUserByAutoDexNum(loginRequest.getAutoDexNum());
		if (loginUserProfile == null || loginUserProfile.getAutoDexNum() == null) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		StringBuilder messageBuilder = new StringBuilder();
		ValidationUtil.validateBooleanObject(loginRequest.getAutoDexNum(), loginUserProfile.isActive(), true, 1, messageBuilder);
		
		if (!passwordEncoder.matches(loginRequest.getPassword(), loginUserProfile.getPassword())) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			String authToken = generateOAuth2AccessToken(loginRequest, loginUserProfile);
			System.out.println("token:" + authToken);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Authorization", authToken);
			Map<Object, Object> responseBody = populateLoginUserResponseBody(loginUserProfile);
			response = new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
		}

		return response;
	}

	private Map<Object, Object> populateLoginUserResponseBody(UserTO user) {
		Map<Object, Object> responseBody = new HashMap<>();
		// responseBody.put("firstName", user.getFirstName());
		user.setPassword(null);
		BasicUserInfo basicUser = new BasicUserInfo();
		basicUser.setId(user.getId());
		responseBody.put("userId", basicUser);
		return responseBody;
	}

	@RequestMapping(value = "/generateotp/{autoDexNum}", method = RequestMethod.GET, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> generateOTP(@PathVariable(value = "autoDexNum") String autoDexNum) {
		
		ResponseEntity<Object> response = null;
		
		// validator.validate(req, 2);
		User user = userService.getUserEntityByAutoDexNum(autoDexNum);
		if (user == null || user.getAutoDexNum() == null || user.getAutoDexNum().trim().isEmpty()) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}
		
		String otp = "";
		if (null != user && user.isActive()) {
			otp = OTPGeneratorUtil.generateOTP(autoDexNum);
			userService.saveOTP(user, otp, autoDexNum);
			
			HttpHeaders headers = new HttpHeaders();
			
			try {
				
				System.out.println("autodex number: " + user.getAutoDexNum());
				
				if(user.getAutoDexNum().length() == 10) {
					naasRestUtil.sendSMS(user.getAutoDexNum(), "AutoDEX Verification Code: " + otp);
					
					System.out.println("SMS sent to: " + user.getAutoDexNum());
				} else if(NaasAppConfig.configMap.get("send.header.otp") != null && 
						NaasAppConfig.configMap.get("send.header.otp").equalsIgnoreCase("Yes")){
					headers.add("OTP", otp);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			response = new ResponseEntity<>(new HashMap<>(), headers, HttpStatus.OK);
		}
		
		return response;
	}

	@RequestMapping(value = "/validateotp", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> validateotp(@RequestBody OTPValidator otpValidator) {
		// validator.validate(req, 2);
		ResponseEntity<Object> response = null;
		
		User user = userService.getUserEntityByAutoDexNum(otpValidator.getAutoDexNum());
		if (user == null || user.getAutoDexNum() == null || user.getAutoDexNum().trim().isEmpty()) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		if (user.isActive() || user.isNewUser()) {
			PasswordRecovery pwdRecovery = userService.findOTPByUserId(user, otpValidator.getOtp());
			if (pwdRecovery == null || otpValidator.getOtp() == null || !otpValidator.getOtp().equals(pwdRecovery.getOtp())) {
				response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				return response;
			}
			
			Date created = pwdRecovery.getCreatedDate();
			long elapsedTime = (System.currentTimeMillis() - created.getTime()) / 60000;
			System.out.println("elapsedTime:::::::::::::::::::::::::::::::::::::::::::: " + elapsedTime);
			if (otpValidator.getOtp().equals(pwdRecovery.getOtp()) && elapsedTime < FIFTEEN_MINUTES) {
				Login login = new Login();
				login.setAutoDexNum(user.getAutoDexNum());
				login.setPassword(user.getPassword());
				UserTO userTo = new UserTO();
				userTo.setAutoDexNum(user.getAutoDexNum());
				userTo.setPassword(user.getPassword());
				userTo.setFirstName(user.getFirstName());
				userTo.setLastName(user.getLastName());
				userTo.setId(user.getId());
				String authToken = generateOAuth2AccessToken(login, userTo);
				System.out.println("token:" + authToken);
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.add("Authorization", authToken);
				Map<Object, Object> responseBody = populateLoginUserResponseBody(userTo);
				response = new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);	
				
				if(user.isNewUser()) {
					userService.makeUserActive(user.getId());
				}
				
			} else {
				response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				return response;
			}
				
		}
		return response;
	}

	private String generateOAuth2AccessToken(Login login, UserTO profile) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		NaasAccountHolderUser userDetails = new NaasAccountHolderUser(profile.getAutoDexNum(), profile.getPassword(),
				authorities);
		userDetails.setUserName(profile.getFirstName()+profile.getLastName());
		userDetails.setId(profile.getId());
		// userDetails.setFirstTimeLogin(profile.isFirstTimeLogin());
		userDetails.setId(profile.getId());

		AuthorizationRequest authorizationRequest = createAuthorizationRequest(profile.getAutoDexNum());

		UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(
				profile.getAutoDexNum(), profile.getPassword());
		OAuth2Authentication authentication = new OAuth2Authentication(authorizationRequest.createOAuth2Request(),
				userAuthentication);
		authentication.setAuthenticated(true);

		authentication.setDetails(userDetails);

		OAuth2AccessToken accessToken = tokenServices.createAccessToken(authentication);
		String authToken = accessToken.getValue();
		logger.info("Access Token Generated for the login-" + accessToken.getValue());
		return authToken;
	}

	private AuthorizationRequest createAuthorizationRequest(String clientName) {
		List<String> clientScopes = Arrays.asList("READ", "WRITE");
		AuthorizationRequest authorizationRequest = new AuthorizationRequest(clientName, clientScopes);
		Map<String, String> azParameters = new HashMap<>(authorizationRequest.getRequestParameters());
		azParameters.put(GRANT_TYPE, "implicit");

		authorizationRequest.setRequestParameters(azParameters);
		return authorizationRequest;
	}
}
