package com.project.bumawiki.domain.coin.presentation.dto;

import com.project.bumawiki.domain.coin.domain.TradeWithoutTradeStatusAndCoinAccountId;

import jakarta.validation.constraints.Positive;

public record TradeRequest(
	@Positive Long coinPrice,
	@Positive Long coinCount
) {
	public TradeWithoutTradeStatusAndCoinAccountId toEntity() {
		return new TradeWithoutTradeStatusAndCoinAccountId(
			coinPrice,
			coinCount
		);
	}
}
