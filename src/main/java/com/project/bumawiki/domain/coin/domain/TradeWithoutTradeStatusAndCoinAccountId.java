package com.project.bumawiki.domain.coin.domain;

import com.project.bumawiki.domain.coin.domain.type.TradeStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TradeWithoutTradeStatusAndCoinAccountId {
	@NotNull
	@Positive
	private Long coinPrice;

	@NotNull
	@Positive
	private Long coinCount;

	public Trade toTrade(CoinAccount coinAccount) {
		return new Trade(
			coinPrice,
			coinCount,
			coinPrice * coinCount,
			TradeStatus.NONE,
			coinAccount.getId()
		);
	}
}
