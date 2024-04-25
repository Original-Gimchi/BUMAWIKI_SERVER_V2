package com.project.bumawiki.domain.coin.domain;

import com.project.bumawiki.domain.coin.domain.type.TradeStatus;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TradeWithoutTradeStatusAndCoinAccountId {
	@Positive
	private Long coinPrice;

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
