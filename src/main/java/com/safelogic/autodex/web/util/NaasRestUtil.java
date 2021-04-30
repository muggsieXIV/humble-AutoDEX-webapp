package com.safelogic.autodex.web.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.safelogic.autodex.web.NaasException;
import com.safelogic.autodex.web.configuration.NaasAppConfig;
import com.safelogic.autodex.web.model.UserDevice;
import com.safelogic.autodex.web.transfer.objects.SmsTO;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

@Component
public class NaasRestUtil {

	private Logger logger = LoggerFactory.getLogger(NaasRestUtil.class);

	public Long getUserId() {
		String userIdStr = MDC.get("userId");
		if (StringUtils.isBlank(userIdStr)) {
			logger.error("NaasRestUtil.getUserId method with no user id info in the request");
			throw new NaasException("Invalid Request Data with no user info. Please contact support.");
		}
		return Long.valueOf(MDC.get("userId"));

	}

	public String getCurrentAccountHolderName() {
		/*
		 * String achName = MDC.get("achName"); if(StringUtils.isBlank(achName))
		 * { logger.error(
		 * "NaasRestUtil.getCurrentAccountHolderName method with no account holder name info in the request"
		 * ); throw new NaasException(
		 * "Invalid Request Data with no account holder info. Please contact support."
		 * ); }
		 */
		return null;
	}

	public String getCurrentUserName() {

		/*
		 * String loginUserName = MDC.get("userName");
		 * 
		 * if(StringUtils.isBlank(loginUserName)) { logger.error(
		 * "NaasRestUtil.getCurrentUserName method with no user name info in the request"
		 * ); throw new NaasException(
		 * "Invalid Request Data with no user name info. Please contact support."
		 * ); }
		 */
		return null;
	}

	public void sendSMS(String phoneNumber, String content) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		String postURL = NaasAppConfig.configMap.get("naas.sms.url");

		System.out.println("NAAS SMS URL: " + postURL);

		SmsTO sms = new SmsTO();
		sms.setPhoneNumber(phoneNumber);
		sms.setTextMessage(content);

		HttpHeaders headers = new HttpHeaders();
		headers.add("clientName", NaasAppConfig.configMap.get("naas.client.name"));
		headers.add("clientId", NaasAppConfig.configMap.get("naas.client.id"));
		headers.add("clientPassword", NaasAppConfig.configMap.get("naas.client.password"));

		HttpEntity<SmsTO> entity = new HttpEntity<SmsTO>(sms, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(postURL, HttpMethod.POST, entity, String.class);

		System.out.println("return string - " + responseEntity.getBody());
	}

	public static void sendPhoneNotification(List<UserDevice> userDevices, String contactToNotify, String message,
												boolean silentNotification) throws Exception {
		try {
		
			for (UserDevice userDevice : userDevices) {
				
				System.out.println("device type: " + userDevice.getDeviceType());

				if (userDevice.getDeviceType().equalsIgnoreCase("ios")) {

					sendIOSNotify(silentNotification, userDevice.getDeviceId(), message);

				} else if (userDevice.getDeviceType().equalsIgnoreCase("andriod")) {
					System.out.println("Need to send notification to Andriod");
				} else {
					System.out.println("Notifications can be sent to either ios or andriod only.");
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void writeImageInBytesToResponse(HttpServletResponse response, byte[] imageInBytes) throws Exception {
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(Base64.encodeBase64(imageInBytes));
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	public byte[] convertImageToByteArray(MultipartFile file) {

		BufferedImage inputBufferedImage;
		BufferedImage resizedImage;
		try {

			if (file.getBytes().length > 10000000) {
				throw new Exception("Image size is greater than 10 MB, Please upload image less than 10MB");
			}
			InputStream is = new ByteArrayInputStream(file.getBytes());
			inputBufferedImage = ImageIO.read(is);

			int type = inputBufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : inputBufferedImage.getType();

			resizedImage = resizeImage(inputBufferedImage, type);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "png", baos);
			return baos.toByteArray();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}
	}

	public BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(64, 64, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 64, 64, null);
		g.dispose();

		return resizedImage;
	}

	private static void sendIOSNotify(boolean silentNotification, String deviceId, String message) throws Exception {
		
		PushNotificationPayload payload = PushNotificationPayload.complex();
		
		System.out.println("silentNotification: " + silentNotification);
		System.out.println("message: " + message);
		
		if(!silentNotification) {
			payload.addBadge(1);			
			payload.addAlert(message);
			payload.addSound("Autodex-Notification-Sound.wav");
			
		} else {
			payload.setContentAvailable(true);			
			payload.addCustomDictionary("operationType", "updateContacts"); 
			List<Integer> values = new ArrayList<Integer>();
			values.add(1);
			values.add(234);
			
			payload.addCustomDictionary("contactIds", values);
		}
		
		String password = "";
		String certFilePath = "/home/ubuntu/AutodexPushNotificationKey_dev.p12";

		List<PushedNotification> NOTIFICATIONS = Push.payload(payload, certFilePath, password, false, deviceId);

		for (PushedNotification NOTIFICATION : NOTIFICATIONS) {

			if (NOTIFICATION.isSuccessful()) {

				System.out.println("PUSH NOTIFICATION SENT SUCCESSFULLY TO: " +

						NOTIFICATION.getDevice().getToken());

			} else {

				String INVALIDTOKEN = NOTIFICATION.getDevice().getToken();

				/*
				 * ADD CODE HERE TO REMOVE INVALIDTOKEN FROM YOUR DATABASE
				 */

				/* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */

				Exception THEPROBLEM = NOTIFICATION.getException();

				THEPROBLEM.printStackTrace();

				/*
				 * IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED BY
				 * APPLE, GET IT
				 */

				ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();

				if (THEERRORRESPONSE != null) {

					System.out.println(THEERRORRESPONSE.getMessage());

				}

			}

		}
	}
}
