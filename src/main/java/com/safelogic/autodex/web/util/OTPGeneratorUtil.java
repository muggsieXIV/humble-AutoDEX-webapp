package com.safelogic.autodex.web.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OTPGeneratorUtil {

	private static Logger logger = LoggerFactory.getLogger(OTPGeneratorUtil.class);
	private static final int OTP_LENGTH = 6;
	private static final String DEFAULT_ALGORITHM = "SHA1PRNG";

	public static String generateOTP(String autoDexNum) {

		StringBuilder generatedToken = new StringBuilder();
		try {
			SecureRandom number = SecureRandom.getInstance(DEFAULT_ALGORITHM);
			for (int i = 0; i < OTP_LENGTH; i++) {
				generatedToken.append(number.nextInt(9));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("Error while generating the OTP for Auto dex number: " + autoDexNum);
		}
		logger.debug("OTP generated successfully for Auto dex number: " + autoDexNum);
		return generatedToken.toString();
	}
}
