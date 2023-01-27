package com.naturegecko.application.utilities;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class StringValidationService {
	static Pattern emailPattern = Pattern.compile("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+");
	// Pattern passwordPattern =
	// Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,128}$");
	static Pattern passwordPattern = Pattern.compile(".{8,128}$");
	static Pattern phoneNumberPattern = Pattern.compile("^[0]\\\\d{1,10}$");
	static Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_-]{6,45}$");

	public static boolean validateEmail(String email) {
		return emailPattern.matcher(email).matches();
	}

	public static boolean validatePassword(String password) {
		return passwordPattern.matcher(password).matches();
	}

	public static boolean validatePhoneNumber(String phoneNumber) {
		return phoneNumberPattern.matcher(phoneNumber).matches();
	}

	public static boolean validateUsername(String username) {
		return usernamePattern.matcher(username).matches();
	}

	// Country Validator

	static Pattern countryNamePattern = Pattern.compile("^[a-zA-Z0-9_-]{6,70}$");
	static Pattern countryPrecisPattern = Pattern.compile("^[a-zA-Z0-9_-]{3}$");

	public static boolean validateCountryName(String countryName) {
		return countryNamePattern.matcher(countryName).matches();
	}

	public static boolean validateCountryPrecis(String countryPrecis) {
		return countryPrecisPattern.matcher(countryPrecis).matches();
	}
	
	public static boolean validateCountryDocument(String url) {
		return url.startsWith("https://docs.google.com/document/");
	}
	
}
