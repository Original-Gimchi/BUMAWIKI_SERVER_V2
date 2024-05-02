package com.project.bumawiki.domain.coin.presentation.dto;

import com.project.bumawiki.domain.coin.domain.TradeVo;

import jakarta.validation.constraints.Positive;

public record TradeRequest(
	@Positive Long coinPrice,
	@Positive Long coinCount
) {
	public TradeVo toEntity() {
		return new TradeVo(
			coinPrice,
			coinCount
		);
	}
}
