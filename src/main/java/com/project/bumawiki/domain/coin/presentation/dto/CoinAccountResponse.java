package com.project.bumawiki.domain.coin.presentation.dto;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.coin.domain.CoinAccount;

public record CoinAccountResponse(
	Long id,
	Long money,
	Long coin,
	LocalDateTime lastRewardedTime
) {

	public static CoinAccountResponse from(CoinAccount coinAccount) {
		return new CoinAccountResponse(
			coinAccount.getId(),
			coinAccount.getMoney(),
			coinAccount.getCoin(),
			coinAccount.getLastRewardedTime()
		);
	}
}
