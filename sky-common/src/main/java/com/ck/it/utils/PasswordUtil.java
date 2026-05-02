package com.ck.it.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

	private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	private PasswordUtil() {
	}

	public static String encode(String rawPassword) {
		return PASSWORD_ENCODER.encode(rawPassword);
	}

	public static boolean matches(String rawPassword, String encodedPassword) {
		if (rawPassword == null || encodedPassword == null) {
			return false;
		}
		return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
	}
}
