package com.project.bumawiki.global.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class RandomUtil {
	public static SecureRandom getRandomInstance() {
		SecureRandom random;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new BumawikiException(ErrorCode.RANDOM_INSTANCE);
		}
		return random;
	}
}
