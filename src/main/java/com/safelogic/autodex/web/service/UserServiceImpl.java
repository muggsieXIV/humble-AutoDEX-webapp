package com.safelogic.autodex.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safelogic.autodex.web.dao.NaasRepository;
import com.safelogic.autodex.web.dao.UserDAO;
import com.safelogic.autodex.web.model.PasswordRecovery;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.model.UserDevice;
import com.safelogic.autodex.web.model.UserNotification;
import com.safelogic.autodex.web.model.UserPreference;
import com.safelogic.autodex.web.model.UserProfileImage;
import com.safelogic.autodex.web.transfer.objects.UserTO;

@Service
@Qualifier("userService")
@Transactional
public class UserServiceImpl implements UserService {

	private NaasRepository<User> userRepo;

	private NaasRepository<UserProfileImage> userProfileImgRepo;
	
	private NaasRepository<UserPreference> userProfilePreferenceRepo;
	
	private NaasRepository<UserNotification> userNotificationRepo;

	private UserDAO userDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Autowired
	@Qualifier("userDAO")
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User createUser(UserTO userTo) {
		User user = new User();
		copyTOToEntity(userTo, user);
		UserProfileImage profileImage = new UserProfileImage();
		profileImage.setUser(user);
		//TODO need to clarify with sashi can one user has one then one profile image.
		List<UserProfileImage> profileImgList = new ArrayList<>();
		profileImgList.add(profileImage);
		user.setUserProfileImages(profileImgList);
		userRepo.create(user);
		return user;
	}

	@Override
	public Boolean updateUser(UserTO userTo) {
		boolean isUpdated = false;
		long userId = userTo.getId();
		User user = userRepo.findById(userId, null);
		if (null != user) {
			copyTOToEntity(userTo, user);
			userRepo.update(user);
			isUpdated = true;
		}
		return isUpdated;
	}

	private void copyTOToEntity(UserTO userTo, User user) {
		// Mandatory fields.
		user.setFirstName(userTo.getFirstName());
		user.setLastName(userTo.getLastName());
		if(null != userTo.getPassword() && !"".equals(userTo.getPassword())){
			String encodedPwd = passwordEncoder.encode(userTo.getPassword());
			user.setPassword(encodedPwd);
		}
		//user.setPassword(userTo.getPassword());
		user.setAutoDexNum(userTo.getAutoDexNum());
		// Optional fields
		user.setImieNumber(userTo.getImieNumber());
		user.setAutoDexNumTag(userTo.getAutoDexNumTag());
		//user.setActive(true);
		user.setMiddleName(userTo.getMiddleName());
		user.setMiddleNameTag(userTo.getMiddleNameTag());
		user.setAddress1(userTo.getAddress1());
		user.setAddress1Tag(userTo.getAddress1Tag());
		user.setAddress2(userTo.getAddress2());
		user.setAddress2Tag(userTo.getAddress2Tag());
		user.setBusinessEmail(userTo.getBusinessEmail());
		user.setBusinessEmailTag(userTo.getBusinessEmailTag());
		user.setCity(userTo.getCity());
		user.setCityTag(userTo.getCityTag());
		user.setFirstNameTag(userTo.getFirstNameTag());
		user.setLastNameTag(userTo.getLastNameTag());
		user.setCompany(userTo.getCompany());
		user.setCompanyTag(userTo.getCompanyTag());
		user.setDesignation(userTo.getDesignation());
		user.setDesignationTag(userTo.getDesignationTag());
		user.setDob(userTo.getDob());
		user.setDobTag(userTo.getDobTag());
		user.setHomeLatitude(userTo.getHomeLatitude());
		user.setHomeLongitude(userTo.getHomeLongitude());
		user.setRoamingHomeLatitude(userTo.getRoamingHomeLatitude());
		user.setRoamingHomeLongtitude(userTo.getRoamingHomeLongtitude());
		user.setPersonalEmail(userTo.getPersonalEmail());
		user.setPersonalEmailTag(userTo.getPersonalEmailTag());
		user.setState(userTo.getState());
		user.setStateTag(userTo.getStateTag());
		user.setZip(userTo.getZip());
		user.setZipTag(userTo.getZipTag());
		user.setNewUser(userTo.isNewUser());
		Date currentTime = new Date();
		user.setCreateDate(currentTime);
		user.setCreatedByUser("System");
		user.setModifiedByUser("System");
		user.setModifiedDate(currentTime);
	}
	
	@Override
	public boolean makeUserActive(Long userId) {
		return userDAO.makeUserActive(userId);
	}

	@Override
	public Boolean deleteUser(Long userId) {
		boolean isUpdated = false;
		User user = userRepo.findById(userId, null);
		if (null != user) {
			user.setActive(Boolean.FALSE);
			userRepo.update(user);
			isUpdated = true;
		}
		return isUpdated;
	}

	@Override
	public UserTO getUserByAutoDexNum(String autoDexNum) {
		User user = getUserEntityByAutoDexNum(autoDexNum);
		UserTO userTO = new UserTO();
		copyEntityToTO(user, userTO);
		return userTO;
	}

	@Override
	public UserTO getUserById(Long userId) {
		User user = userRepo.findById(userId, null);
		UserTO userTO = new UserTO();
		copyEntityToTO(user, userTO);
		return userTO;
	}
	
	@Override
	public User getUser(Long userId) {
		return userRepo.findById(userId, null);
	}

	@Override
	public UserProfileImage getUserProfileImgById(Long userId) {
		List<UserProfileImage> userProfileImgList = userProfileImgRepo.findAllByUserId(userId);
		UserProfileImage image=null;
		if(null != userProfileImgList && !userProfileImgList.isEmpty()){
			image = userProfileImgList.get(0);
		}
		return image;
	}

	@Override
	public boolean updateUserProfileImg(UserProfileImage profileImg) {
		userProfileImgRepo.update(profileImg);
		return true;
	}
	
	@Override
	public UserPreference getUserPreference(Long userId) {
		
		return userDAO.getUserPreference(userId);
	}
	
	@Override
	public UserPreference createUserPreference(UserPreference userPreference) {
		
		return  userProfilePreferenceRepo.create(userPreference);
		
	}
	
	@Override
	public List<UserDevice> getChangeNumberContactDevices(User user) {
		return userDAO.getChangeNumberNotications(user); 
	}
	
	@Override
	public boolean createOrUpdateUserPreference(Long userId, UserPreference userPreference) {
		return userDAO.createOrUpdateUserPreference(userId, userPreference);
	}

	@Override
	public boolean updateUserGeoLocation(Long userId, String latitude, String longitude) {
		return userDAO.updateUserGeoLocation(userId, latitude, longitude);
	}
	
	@Override
	public boolean addUserDevice(Long userId, UserDevice userDevice) {
		return userDAO.addUserDevice(userId, userDevice);
	}
	
	@Override
	public boolean deleteUserDevice(UserDevice userDevice) {
		return userDAO.deleteUserDevice(userDevice);
	}
	
	@Override
	public boolean updateUserPassword(Long userId, String newPassword) {
		
		String encodedPwd = passwordEncoder.encode(newPassword);
		
		return userDAO.updateUserPassword(userId, encodedPwd);
	}
	
	@Override
	public boolean updateAutoDex(Long userId, String newNumber) {
		
		return userDAO.updateAutodexNumber(userId, newNumber);
	}

	@Override
	public PasswordRecovery findOTPByUserId(User user, String otp) {
		return userDAO.findOTPByUserId(user);
	}

	@Override
	public boolean saveOTP(User user, String otp, String autoDexNum) {
		return userDAO.saveOTP(user, otp, autoDexNum);
	}

	@Override
	public User getUserEntityByAutoDexNum(String autoDexNum) {
		List<User> userList = userRepo.findByAttribute("autoDexNum", autoDexNum);
		
		User user = null;
		if(userList.size() > 0) {
			user = userList.get(0);
		}
		
		return user;
	}
	
	@Override
	public boolean createUserNotification(User user, String type, String message) {
		
		UserNotification notification = new UserNotification();
		
		try {
			notification.setMessage(message);
			notification.setType(type);
			notification.setRead(false);
			notification.setContactId(0L);
			
			notification.setUserId(user.getId());
			
			Date currentDate = new Date();
			notification.setCreateDate(currentDate);
			notification.setModifiedDate(currentDate);
			notification.setCreatedByUser("system");
			notification.setModifiedByUser("system");
			
			userNotificationRepo.create(notification);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
		
		return true;		
	}
	
	@Override
	public boolean updateUserNotificationRead(Long id, Long userId, boolean read) {
		return userDAO.updateUserNotificationRed(id, userId, read);
	}
	
	@Override
	public boolean deleteUserNotificationRead(Long id, Long userId) { 
		return userNotificationRepo.deleteById(id, userId);
	}
	
	@Override
	public List<UserNotification> getAllUserNotifications(Long userId) { 
		return userNotificationRepo.findAllByUserId(userId);
	}
	
	private void copyEntityToTO(User source, UserTO destination) {
		if(source == null || destination == null) {
			return;
		}
		
		destination.setId(source.getId());
		// Mandatory fields.
		destination.setFirstName(source.getFirstName());
		destination.setLastName(source.getLastName());
		// Optional fields
		destination.setImieNumber(source.getImieNumber());
		destination.setAutoDexNum(source.getAutoDexNum());
		destination.setAutoDexNumTag(source.getAutoDexNumTag());
		destination.setActive(source.isActive());
		destination.setMiddleName(source.getMiddleName());
		destination.setMiddleNameTag(source.getMiddleNameTag());
		destination.setAddress1(source.getAddress1());
		destination.setAddress1Tag(source.getAddress1Tag());
		destination.setAddress2(source.getAddress2());
		destination.setAddress2Tag(source.getAddress2Tag());
		destination.setBusinessEmail(source.getBusinessEmail());
		destination.setBusinessEmailTag(source.getBusinessEmailTag());
		destination.setCity(source.getCity());
		destination.setCityTag(source.getCityTag());
		destination.setFirstNameTag(source.getFirstNameTag());
		destination.setLastNameTag(source.getLastNameTag());
		destination.setCompany(source.getCompany());
		destination.setCompanyTag(source.getCompanyTag());
		destination.setDesignation(source.getDesignation());
		destination.setDesignationTag(source.getDesignationTag());
		destination.setDob(source.getDob());
		destination.setDobTag(source.getDobTag());
		destination.setHomeLatitude(source.getHomeLatitude());
		destination.setHomeLongitude(source.getHomeLongitude());
		destination.setRoamingHomeLatitude(source.getRoamingHomeLatitude());
		destination.setRoamingHomeLongtitude(source.getRoamingHomeLongtitude());
		destination.setPersonalEmail(source.getPersonalEmail());
		destination.setPersonalEmailTag(source.getPersonalEmailTag());
		destination.setState(source.getState());
		destination.setStateTag(source.getStateTag());
		destination.setZip(source.getZip());
		destination.setZipTag(source.getZipTag());
		destination.setNewUser(source.isNewUser());
		//TODO need to remove this below line
		destination.setPassword(source.getPassword());
		// Date currentTime = new Date();
		// destination.set(currentTime);
		// destion.sination.setCreatedByUser("System");
		// destinatetModifiedByUser("System");
		// destination.setModifiedDate(currentTime);
	}
	
	@Autowired
	@Qualifier("naasRepository")
	public void setUserRepo(NaasRepository<User> userRepo) {
		this.userRepo = userRepo;
		this.userRepo.setType(User.class);
	}

	@Autowired
	@Qualifier("naasRepository")
	public void setUserProfileImgRepo(NaasRepository<UserProfileImage> userProfileImgRepo) {
		this.userProfileImgRepo = userProfileImgRepo;
		this.userProfileImgRepo.setType(UserProfileImage.class);
	}
	
	@Autowired
	@Qualifier("naasRepository")
	public void setUserProfilePreferenceRepo(NaasRepository<UserPreference> userProfilePreferenceRepo) {
		this.userProfilePreferenceRepo = userProfilePreferenceRepo;
		this.userProfilePreferenceRepo.setType(UserPreference.class);
	}

	@Autowired
	@Qualifier("naasRepository")
	public void setUserNotificationRepo(NaasRepository<UserNotification> userNotificationRepo) {
		this.userNotificationRepo = userNotificationRepo;
		this.userNotificationRepo.setType(UserNotification.class);
	}
}
