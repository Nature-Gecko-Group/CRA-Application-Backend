package com.naturegecko.application.utilities;

import java.time.LocalDate;
import java.util.Random;

public class RandomStringGenerator {

	private static final int firstAscii = 48;
	private static final int lastAscii = 122;

	// Generate name for tracks and music.
	public static String generateName(int nameLength, String prefix) {
		return prefix + dateTimeGatherer() + generateString(nameLength);
	}

	private static String generateString(int textLength) {
		Random randomString = new Random();
		StringBuilder buffer = new StringBuilder(textLength);

		for (int i = 0; i < textLength; i++) {
			int randomizedInt = firstAscii + (int) (randomString.nextFloat() * (lastAscii - firstAscii + 1));
			if ((randomizedInt >= 91 && randomizedInt <= 96) || (randomizedInt >= 58 && randomizedInt <= 64)) {
				randomizedInt = firstAscii;
			}
			buffer.append((char) randomizedInt);
		}
		return buffer.toString();
	}

	private static String dateTimeGatherer() {
		String currentDate = LocalDate.now().toString();
		return currentDate.substring(0, 4) + currentDate.substring(5, 7) + currentDate.substring(8);
	}
}
