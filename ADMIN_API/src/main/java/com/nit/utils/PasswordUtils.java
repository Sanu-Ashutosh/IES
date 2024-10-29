package com.nit.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {
	 public static String generateTempPassword(int length) {
	        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
	        Random random = new Random();
	        StringBuilder password = new StringBuilder();

	        for (int i = 0; i < length; i++) {
	            password.append(chars.charAt(random.nextInt(chars.length())));
	        }

	        return password.toString();
	    }

}
