package com.project.bumawiki.domain.coin.presentation.dto;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.coin.domain.Price;

public record PriceResponse(
	Long price,
	LocalDateTime startedTime
) {
	public static PriceResponse from(Price price) {
		return new PriceResponse(
			price.getPrice(),
			price.getStartedTime()
		);
	}
}
