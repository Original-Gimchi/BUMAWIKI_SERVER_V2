package com.project.bumawiki.domain.coin.implementation;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class CoinAccountValidator {

	private final CoinAccountReader coinAccountReader;

	public void checkAlreadyCreatedAccount(Long userId) {
		if (coinAccountReader.existsByUserId(userId)) {
			throw new BumawikiException(ErrorCode.ALREADY_CREATED);
		}
	}

	public void checkAlreadyRewardedToday(CoinAccount account) {
		if (account.wasRewardedToday()) {
			throw new BumawikiException(ErrorCode.ALREADY_AWARDED);
		}
	}
}
