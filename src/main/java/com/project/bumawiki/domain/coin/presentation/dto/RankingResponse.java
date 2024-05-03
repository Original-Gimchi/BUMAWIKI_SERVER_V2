package com.project.bumawiki.domain.coin.presentation.dto;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.user.domain.User;

public record RankingResponse(
	Long coinAccountId,
	Long coin,
	Long money,
	Long totalMoney,
	Long userId,
	String username
) {
	public RankingResponse(CoinAccount coinAccount, Long nowPrice, User user) {
		this(coinAccount.getId(), coinAccount.getCoin(), coinAccount.getMoney(),
			(coinAccount.getMoney() + coinAccount.getCoin() * nowPrice), user.getId(), user.getName());
	}
}
