package com.safelogic.autodex.web.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.safelogic.autodex.web.model.PasswordRecovery;
import com.safelogic.autodex.web.model.User;
import com.safelogic.autodex.web.model.UserDevice;
import com.safelogic.autodex.web.model.UserNotification;
import com.safelogic.autodex.web.model.UserPreference;

@Repository("userDAO")
public class UserDAOImpl extends NaasRepositoryImpl<User> implements UserDAO {

	@PersistenceContext
	private EntityManager em;
	
	/*
	private static String GET_CHANGE_NUMBER_NOTIFICATION = "select userDevice from Contacts contacts, " + 
			" User user, UserDevice userDevice " +
			" where contacts.userId = ? and user.id <> contacts.userId and contacts.phoneNumber = user.autodexnum " +
			" and contacts.userId = userDevice.userId" ;
			*/

	private static String GET_CHANGE_NUMBER_NOTIFICATION = "select userDevice from UserDevice userDevice where userDevice.user = ? " ;
	
	@Override
	public boolean updateUserGeoLocation(Long userId, String latitude, String longitude) {
		Query query = em.createNativeQuery("update User user set user.roamingHomeLatitude=?, user.roamingHomeLongtitude=? where user.id=?");
		
		query.setParameter(1, latitude);
		query.setParameter(2, longitude);
		query.setParameter(3, userId);
		query.executeUpdate();
		
		return true;
	}
	
	@Override
	public boolean updateUserPassword(Long userId, String newPassword) {
		Query query = em.createNativeQuery("update User user set user.password = ? where user.id = ?");
		
		query.setParameter(1, newPassword);
		query.setParameter(2, userId);
		query.executeUpdate();
		
		return true;
	}
	
	@Override
	public boolean updateAutodexNumber(Long userId, String newNumber) {
		Query query = em.createNativeQuery("update User user set user.autoDexNum = ? where user.id = ?");
		
		query.setParameter(1, newNumber);
		query.setParameter(2, userId);
		query.executeUpdate();
		
		return true;
	}
	
	@Override
	public boolean makeUserActive(Long userId) {
		Query query = em.createNativeQuery("update User user set user.active = 1, user.new = 0 where user.id = ?");
		
		query.setParameter(1, userId);
		query.executeUpdate();
		
		return true;
	}
	
	@Override
	public boolean updateUserNotificationRed(Long id, Long userId, boolean read) {
		Query query = em.createNativeQuery("update UserNotifications userNotification set userNotification.readFlag = ? "
				+ "	where userNotification.id = ? and userNotification.userId = ?");
		
		query.setParameter(1, read);
		query.setParameter(2, id);
		query.setParameter(3, userId);
		query.executeUpdate();
		
		return true;
	}
	
	@Override
	public UserPreference getUserPreference(Long userId) {
		
		TypedQuery<UserPreference> query = em.createQuery("Select userPreference from UserPreference userPreference where userPreference.userId = ?",
												UserPreference.class);
		
		query.setParameter(1, userId);
		
		List<UserPreference> userPrefList = query.getResultList();
		if(null != userPrefList && !userPrefList.isEmpty())
		{
			return query.getResultList().get(0);
		}
		
		return null;
	}
	
	@Override
	public boolean createOrUpdateUserPreference(Long userId, UserPreference userPreference) {
		
		TypedQuery<UserPreference> query = em.createQuery("Select userPreference from UserPreference userPreference where userPreference.userId = ?",
				UserPreference.class);

		query.setParameter(1, userId);

		UserPreference userPref = null;
		List<UserPreference> userPrefList = query.getResultList();
		userPreference.setUserId(userId);
		Date currentDate = new Date();
		if(null != userPrefList && !userPrefList.isEmpty())
		{
			userPref = query.getResultList().get(0);
			
			userPreference.setId(userPref.getId());
			userPreference.setCreateDate(userPref.getCreateDate());
			userPreference.setModifiedDate(currentDate);
			
			em.merge(userPreference);
		} else {
			
			userPreference.setCreateDate(currentDate);
			userPreference.setModifiedDate(currentDate);
			
			em.persist(userPreference);
		}
		
		return true;
	}

	@Override
	public boolean saveOTP(User user, String otp, String autoDexNum) {
		try {
			PasswordRecovery pwdRecovery = findOTPByUserId(user);
			System.out.println("pwdRecovery: " + pwdRecovery);
			if (null == pwdRecovery || pwdRecovery.getUser() == null) {
				pwdRecovery = new PasswordRecovery();
				pwdRecovery.setOtp(otp);
				pwdRecovery.setUser(user);
				pwdRecovery.setRetryCount(0);
				pwdRecovery.setCreatedDate(new Date());
				em.persist(pwdRecovery);
			} else {
				pwdRecovery.setOtp(otp);
				pwdRecovery.setUser(user);
				pwdRecovery.setCreatedDate(new Date());
				em.merge(pwdRecovery);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public PasswordRecovery findOTPByUserId(User user) {
		PasswordRecovery pwdRecovery =null;
		
		try {
			TypedQuery<PasswordRecovery> query = em.createQuery("Select pwdRecovery from PasswordRecovery pwdRecovery where pwdRecovery.user = :userId",PasswordRecovery.class);
			query.setParameter("userId", user);
			//query.setParameter("otp", otp);
			
			List<PasswordRecovery> pwdList = query.getResultList();
			if(null != pwdList && !pwdList.isEmpty())
			{
				pwdRecovery = query.getResultList().get(0);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pwdRecovery;
	}
	
	@Override
	public boolean addUserDevice(Long userId, UserDevice userDevice) {
		
		try {
			User user = em.find(User.class, userId); 
			if(user == null) {
				return false;
			}
	
			UserDevice userDev = getUserDevice(user, userDevice.getDeviceId());
			if(userDev == null) {
				
				userDev = new UserDevice();
				userDev.setDeviceId(userDevice.getDeviceId());
				userDev.setDeviceType(userDevice.getDeviceType());
				userDev.setUser(user);
				userDev.setCreateDate(new Date());
				em.persist(userDev);
			} else {
				userDev.setDeviceId(userDevice.getDeviceId());
				userDev.setDeviceType(userDevice.getDeviceType());
				userDev.setCreateDate(new Date());
				em.merge(userDev);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public List<UserDevice> getChangeNumberNotications(User user) {
		List<UserDevice> devices = new ArrayList<UserDevice>();
		
		try {
			TypedQuery<UserDevice> query = em.createQuery(GET_CHANGE_NUMBER_NOTIFICATION, UserDevice.class);
			
			query.setParameter(1, user);
			
			List<UserDevice> userDeviceList = query.getResultList();
			if(null != userDeviceList && !userDeviceList.isEmpty())
			{
				devices = query.getResultList();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return devices;
	}
	
	@Override
	public boolean deleteUserDevice(UserDevice userDevice) {
		
		try {
	
			UserDevice userDev = getUserDevice(userDevice.getDeviceId());
			if(userDev != null) {
				em.remove(userDev);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public UserDevice getUserDevice(User user, String deviceId) {
		
		TypedQuery<UserDevice> query = em.createQuery(
				"Select userDevice from UserDevice userDevice where userDevice.user = ? and userDevice.deviceId = ?",
												UserDevice.class);
		
		query.setParameter(1, user);
		query.setParameter(2, deviceId);
		
		List<UserDevice> userDeviceList = query.getResultList();
		if(null != userDeviceList && !userDeviceList.isEmpty())
		{
			return query.getResultList().get(0);
		}
		
		return null;
	}
	
	public UserDevice getUserDevice(String deviceId) {
		
		TypedQuery<UserDevice> query = em.createQuery(
				"Select userDevice from UserDevice userDevice where userDevice.deviceId = ?",
												UserDevice.class);
		query.setParameter(1, deviceId);
		
		List<UserDevice> userDeviceList = query.getResultList();
		if(null != userDeviceList && !userDeviceList.isEmpty())
		{
			return query.getResultList().get(0);
		}
		
		return null;
	}
}
