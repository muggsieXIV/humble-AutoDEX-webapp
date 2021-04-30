package com.safelogic.autodex.web.dao;

import java.util.List;

import com.safelogic.autodex.web.model.PasswordRecovery;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.model.UserDevice;
import com.safelogic.autodex.web.model.UserNotification;
import com.safelogic.autodex.web.model.UserPreference;

public interface UserDAO {

	public boolean updateUserGeoLocation(Long userId, String latitude,String longitude);
	
	public boolean saveOTP(User user, String otp,String autoDexNum);
	
	public PasswordRecovery findOTPByUserId(User user);
	
	public boolean updateUserPassword(Long userId, String newPassword);
	
	public boolean createOrUpdateUserPreference(Long userId, UserPreference userPreference);
	
	public UserPreference getUserPreference(Long userId);

	public boolean addUserDevice(Long userId, UserDevice userDevice);
	
	public boolean makeUserActive(Long userId);

	public boolean updateUserNotificationRed(Long id, Long userId, boolean read);

	public boolean deleteUserDevice(UserDevice userDevice);

	public boolean updateAutodexNumber(Long userId, String newNumber);

	public List<UserDevice> getChangeNumberNotications(User user);
}
