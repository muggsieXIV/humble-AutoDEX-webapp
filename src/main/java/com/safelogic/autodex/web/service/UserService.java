package com.safelogic.autodex.web.service;

import java.util.List;

import com.safelogic.autodex.web.model.PasswordRecovery;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.model.UserDevice;
import com.safelogic.autodex.web.model.UserNotification;
import com.safelogic.autodex.web.model.UserPreference;
import com.safelogic.autodex.web.model.UserProfileImage;
import com.safelogic.autodex.web.transfer.objects.UserTO;

public interface UserService {

	public User createUser(UserTO userTo);
	public Boolean updateUser(UserTO userTo);
	public Boolean deleteUser(Long userId);
	public UserTO getUserByAutoDexNum(String autoDexNum);
	public User getUserEntityByAutoDexNum(String autoDexNum);
	public UserTO getUserById(Long userId);
	public UserProfileImage getUserProfileImgById(Long userId);
	public boolean updateUserProfileImg(UserProfileImage profileImg);
	public boolean updateUserGeoLocation(Long userId, String latitude,String longitude);
	public boolean saveOTP(User user, String otp, String autoDexNum);
	public PasswordRecovery findOTPByUserId(User user, String otp);
	public boolean updateUserPassword(Long userId, String newPassword);
	public UserPreference createUserPreference(UserPreference userPreference);
	public boolean createOrUpdateUserPreference(Long userId, UserPreference userPreference);
	public UserPreference getUserPreference(Long userId);
	public boolean addUserDevice(Long userId, UserDevice userDevice);
	public boolean makeUserActive(Long userId);
	public List<UserNotification> getAllUserNotifications(Long userId);
	public boolean updateUserNotificationRead(Long id, Long userId, boolean read);
	public boolean deleteUserNotificationRead(Long id, Long userId);
	public boolean deleteUserDevice(UserDevice userDevice);
	public boolean updateAutoDex(Long userId, String newNumber);
	public List<UserDevice> getChangeNumberContactDevices(User user);
	public User getUser(Long userId);
	public boolean createUserNotification(User user, String type, String message);
}
