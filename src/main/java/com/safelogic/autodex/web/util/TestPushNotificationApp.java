package com.safelogic.autodex.web.util;

import java.util.ArrayList;
import java.util.List;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

public class TestPushNotificationApp {

	public static void main(String[] args) {

		try {

			PushNotificationPayload payload = PushNotificationPayload.complex();
			
			// Following 3 lines are not to be sent during silent notification
			/*
			payload.addBadge(1);			
			payload.addAlert("Hello Hello Hello !!!");
			payload.addSound("Autodex-Notification-Sound.wav");
			*/
			
			// for silent notification only
			
			payload.setContentAvailable(true);			
			payload.addCustomDictionary("operationType", "updateContacts"); 
			List<Integer> values = new ArrayList<Integer>();
			values.add(1);
			values.add(234);
			
			payload.addCustomDictionary("contactIds", values);
			
			
			System.out.println(payload.toString());

			String deviceId = "90405075ab7b5599bafad15a10fa9e6d051b2d86cccf5bbcb5195679bcd49ec5";
			String password = "";
			String certFilePath = "C:/Users/Sashi/OneDrive/Projects/AutoDEX/AutodexPushNotificationKey_dev.p12";

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

		} catch (CommunicationException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (KeystoreException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}