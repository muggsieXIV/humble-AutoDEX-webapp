package com.safelogic.autodex.web.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.safelogic.autodex.web.NaasException;
import com.safelogic.autodex.web.NaasURIConstants;
import com.safelogic.autodex.web.configuration.NaasAppConfig;
import com.safelogic.autodex.web.model.BasicUserInfo;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.model.UserDevice;
import com.safelogic.autodex.web.model.UserNotification;
import com.safelogic.autodex.web.model.UserPreference;
import com.safelogic.autodex.web.model.UserProfileImage;
import com.safelogic.autodex.web.service.UserService;
import com.safelogic.autodex.web.transfer.objects.ChangeNumberTO;
import com.safelogic.autodex.web.transfer.objects.LocationTO;
import com.safelogic.autodex.web.transfer.objects.Login;
import com.safelogic.autodex.web.transfer.objects.UserTO;
import com.safelogic.autodex.web.util.NaasRestUtil;
import com.safelogic.autodex.web.util.OTPGeneratorUtil;
import com.safelogic.autodex.web.validator.UserValidator;
import com.safelogic.autodex.web.validator.ValidationEnum;

@RestController
@RequestMapping(value = NaasURIConstants.autodexUserBase)
public class UserRestController {

	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	UserValidator validator = new UserValidator();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JmsTemplate jmsQueueTemplate;

	private NaasRestUtil naasRestUtil;

	@Autowired
	@Qualifier("naasRestUtil")
	public void setNaasRestUtil(NaasRestUtil naasRestUtil) {
		this.naasRestUtil = naasRestUtil;
	}
	
	@RequestMapping(value = "/createProfile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BasicUserInfo createUserProfile(@RequestBody UserTO userTO, HttpSession session, HttpServletResponse response) throws Exception {
		
		BasicUserInfo basicUser = new BasicUserInfo();
		validator.validate(userTO, ValidationEnum.SAVE.getVal());
		
		try {
			
			UserTO userDB = userService.getUserByAutoDexNum(userTO.getAutoDexNum());
			if(userDB != null && userDB.getAutoDexNum() != null && userDB.getAutoDexNum().equals(userTO.getAutoDexNum())) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return null;
			}
			
			logger.debug("Create user service called for User: " + userTO.getFirstName() + " " + userTO.getLastName());
			userTO.setNewUser(true);
			User user = userService.createUser(userTO);
			basicUser.setId(user.getId());
			
			String otp = "";
			if (null != user) {
				otp = OTPGeneratorUtil.generateOTP(user.getAutoDexNum());
				userService.saveOTP(user, otp, user.getAutoDexNum());
				
				if(user.getAutoDexNum().length() == 10) {
					naasRestUtil.sendSMS(user.getAutoDexNum(), "AutoDEX Verification Code: " + otp);
				}
				
				if(NaasAppConfig.configMap.get("send.header.otp") != null && 
						NaasAppConfig.configMap.get("send.header.otp").equalsIgnoreCase("Yes")){
					response.addHeader("OTP", otp);
				}
			}

		} catch (Exception exp) {
			exp.printStackTrace();
			logger.error("Error happened in create user = " + " , Please contact support.");
		}
		
		return basicUser;
	}

	

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean updateUserProfile(@RequestBody UserTO userTO, HttpSession session) throws Exception {
		boolean isProfileUpdated = false;
//		if (null == userTO || null == userTO.getId() || null == userTO.getFirstName() || null == userTO.getLastName()
//				|| null == userTO.getPassword() || null == userTO.getAutoDexNum()) {
//			throw new NaasException("Invalid request for update user.");
//		}
		validator.validate(userTO, ValidationEnum.UPDATE.getVal());
		try {
			logger.debug("Update user service called for User: " + userTO.getFirstName() + " " + userTO.getLastName()
					+ " id: " + userTO.getId());
			isProfileUpdated = userService.updateUser(userTO);

		} catch (Exception ex) {
			logger.error("Error happened in create user = " + " , Please contact support.", ex);
		}
		return isProfileUpdated;
	}

	@RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteUser() {
		Long userId = naasRestUtil.getUserId();
		if (null == userId) {
			throw new NaasException("Invalid request for delete user.");
		}
		logger.debug("deleteUser request with userId = " + userId);
		boolean userDeleted = false;
		try {
			userDeleted = userService.deleteUser(userId);
		} catch (Exception exp) {
			logger.error("Error happened in delete user for userId = " + userId, exp);
		}
		return userDeleted;
	}

	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO getUserById() {
		Long userId = naasRestUtil.getUserId();
		if (null == userId) {
			throw new NaasException("Invalid request for delete user.");
		}
		UserTO userTo = null;
		logger.debug("deleteUser request with userId = " + userId);
		try {
			userTo = userService.getUserById(userId);
		} catch (Exception exp) {
			logger.error("Error happened in delete user for userId = " + userId, exp);
		}
		return userTo;
	}

	@RequestMapping(value = "/getByAutoDexNum/{autoDexNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO getUserByAutoDexNum(@PathVariable(value = "autoDexNum") String autoDexNum) {
		if (null == autoDexNum || "" == autoDexNum) {
			throw new NaasException("Invalid request for delete user.");
		}
		UserTO userTo = null;
		logger.debug("deleteUser request with userId = " + autoDexNum);
		try {
			userTo = userService.getUserByAutoDexNum(autoDexNum);
		} catch (Exception exp) {
			logger.error("Error happened in delete user for userId = " + autoDexNum, exp);
		}
		return userTo;
	}

	@RequestMapping(value = "/profileimage",method = RequestMethod.GET)
	public void getProfileImage(HttpServletResponse response) throws Exception {
		Long userId = naasRestUtil.getUserId();
		logger.debug("getProfileImage request with userId = " + userId);
		UserProfileImage dbUser = userService.getUserProfileImgById(userId);
		// validator.validate(dbContact, 2);// check for not null contact for
		byte[] imageInBytes = dbUser.getProfilePic();
		
		if(imageInBytes != null && imageInBytes.length > 0)
			naasRestUtil.writeImageInBytesToResponse(response, imageInBytes);
	}

	@RequestMapping(value = "/profileimage", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody boolean updateProfileImage(@RequestParam("file") MultipartFile file) {
		Long userId = naasRestUtil.getUserId();
		logger.debug("updateProfileImage request with userId = " + userId);
		UserProfileImage dbUser = userService.getUserProfileImgById(userId);
		
		byte[] fileBytes = naasRestUtil.convertImageToByteArray(file);
		dbUser.setProfilePic(fileBytes);
		userService.updateUserProfileImg(dbUser);
		return true;
	}
	
	@RequestMapping(value = "/geolocation",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean updateUserGeoLocation(@RequestBody LocationTO location, HttpSession session) throws Exception {
		
		System.out.println("Notify user: " + location.isNotifyContacts());
		
		Long userId = naasRestUtil.getUserId();
		boolean updated = false;
		
		if (null ==location || null == location.getLongtitude() || null == location.getLatitude()) {
			throw new NaasException("Invalid request for update user geo location.");
		}
		try {
			
			updated = userService.updateUserGeoLocation(userId, location.getLatitude(), location.getLongtitude());

		} catch (Exception exp) {
			logger.error("Error happened in update user Geo Location. Please contact support.");
		}
		return updated;
	}
	
	@RequestMapping(value = "/addUserDevice",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean addUserDevice(@RequestBody UserDevice userDevice, HttpSession session) throws Exception {
		Long userId = naasRestUtil.getUserId();
		boolean updated = false;
		if (null == userDevice || StringUtils.isEmpty(userDevice.getDeviceId()) || StringUtils.isEmpty(userDevice.getDeviceType())) {
			logger.debug("Device Id or Device Type is empty.");
			return false;
		}
		
		try {
			
			updated = userService.addUserDevice(userId, userDevice);

		} catch (Exception exp) {
			logger.debug("Exception occurred during add user device. Please contact support.");
		}
		
		return updated;
	}
	
	@RequestMapping(value = "/changeAutodexNumber",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean changeAutodexNumber(@RequestBody ChangeNumberTO changeNumber, HttpSession session) throws Exception {
		System.out.println("In changeAutodexNumber():::::::::::::::::::::::::::::::::::::::::");
		Long userId = naasRestUtil.getUserId();
		
		User user = userService.getUser(userId);
		
		boolean changed = false;
		if (null == changeNumber || StringUtils.isEmpty(changeNumber.getNewAutoDexNum()) || StringUtils.isEmpty(changeNumber.getContactToNotify())) {
			logger.debug("Validation error. AutoDex Number and/or Contact to Notify fields are empty.");
			return false;
		}
		
		// TODO: validation needed - check newAutoDEXNum is already used by another user
		
		try {
			
			System.out.println("before updateAutoDex :::::::::::::::::::::::::::::::::::::::::");
			
			changed = userService.updateAutoDex(userId, changeNumber.getNewAutoDexNum());
			
			System.out.println("after updateAutoDex :::::::::::::::::::::::::::::::::::::::::");
			
			String msg = "Your contact " + user.getFirstName() + " " + user.getLastName() + " mobile number has changed from " +
					user.getAutoDexNum() + " to " + changeNumber.getNewAutoDexNum();
			
			userService.createUserNotification(user, "change-contact-info", msg);
			
			List<UserDevice> userDevices = userService.getChangeNumberContactDevices(user);
			
			NaasRestUtil.sendPhoneNotification(userDevices, changeNumber.getContactToNotify(), 
									msg, false);
			
			/*
			String msg = " Your contact (" + user.getFirstName() + " " + user.getLastName() + ") mobile number has changed from " +
							user.getAutoDexNum() + " to " + changeNumber.getNewAutoDexNum();
			
			final AtomicReference<TextMessage> message = new AtomicReference<TextMessage>();
			jmsQueueTemplate.send(new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage = session.createTextMessage();
					textMessage.setText("PhoneNotify~~" + userId + "~~" + changeNumber.getContactToNotify() + "~~" + msg + "~~" + "No");
					message.set(textMessage);
					return message.get();
				}
			});		
			*/
			

		} catch (Exception exp) {
			
			logger.debug("Exception occurred during add user device. Please contact support.");
			
		}
		
		return changed;
	}
	
	@RequestMapping(value = "/logout",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean logoutUser(@RequestBody UserDevice userDevice) throws Exception {
		boolean updated = false;
		if (StringUtils.isEmpty(userDevice.getDeviceId())) {
			logger.debug("Device Id is empty.");
			return false;
		}
		
		try {
			
			updated = userService.deleteUserDevice(userDevice);

		} catch (Exception exp) {
			return false;
		}
		
		return updated;
	}
	
	@RequestMapping(value = "/password",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean updateUserPassword(@RequestBody Login loginRequest, HttpSession session) throws Exception {
		
		Long userId = naasRestUtil.getUserId();
		boolean updated = false;
		if (null == loginRequest.getPassword() || loginRequest.getPassword().trim().length() < 4) { 
			throw new NaasException("Invalid password. Password should be at least 4 characters long.");
		}
		
		try {
			
			updated = userService.updateUserPassword(userId, loginRequest.getPassword());

		} catch (Exception exp) {
			logger.error("Error happened in update user password. Please contact support.");
		}
		return updated;
	}
	
	@RequestMapping(value = "/preference",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserPreference getUserPreference(HttpSession session) throws Exception {
		
		Long userId = naasRestUtil.getUserId();
		
		return userService.getUserPreference(userId);
		
	}
	
	@RequestMapping(value = "/preference",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean saveUserPreference(@RequestBody UserPreference userPreference, HttpSession session) throws Exception {
		
		Long userId = naasRestUtil.getUserId();
		
		boolean updated = false;
		if (null == userPreference) { 
			throw new NaasException("Invalid User Preference.");
		}
		
		try {
			
			updated = userService.createOrUpdateUserPreference(userId, userPreference);

		} catch (Exception exp) {
			exp.printStackTrace();
			logger.error("Error happened in update user password. Please contact support.");
		}
		
		return updated;
	}
	
	@RequestMapping(value="/sendTestSMS", method=RequestMethod.POST)
	public @ResponseBody String sendTestSMS(@RequestParam String smsContent, @RequestParam String testPhoneNumber){
		
		System.out.println("In sendTestSms... ");
		String testSmsContent = "Test SMS - " + smsContent;
		
		final AtomicReference<TextMessage> message = new AtomicReference<TextMessage>();
		jmsQueueTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage();
				textMessage.setText("TestSms~~" + testPhoneNumber + "~~" + testSmsContent);
				message.set(textMessage);
				return message.get();
			}
		});		
		
		System.out.println("testPhoneNumber: " + testPhoneNumber);
		
		return "Test SMS Sent";
	}
	
	@RequestMapping(value = "/notification/list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<UserNotification> getUserNotification() throws Exception {
		
		Long userId = naasRestUtil.getUserId();
		
		return userService.getAllUserNotifications(userId);
		
	}
	
	@RequestMapping(value = "/notification", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean updateUserNotificationRead(@RequestBody UserNotification userNotification) throws Exception {
		
		Long userId = naasRestUtil.getUserId();
		
		return userService.updateUserNotificationRead(userNotification.getId(), userId, userNotification.isRead());
		
	}
	
	@RequestMapping(value = "/notification/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean deleteUserNotification(@PathVariable(value = "id") Long id) throws Exception {
		
		Long userId = naasRestUtil.getUserId();
		
		return userService.deleteUserNotificationRead(id, userId);
		
	}
}
