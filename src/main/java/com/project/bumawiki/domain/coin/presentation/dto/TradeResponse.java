package com.project.bumawiki.domain.coin.presentation.dto;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;

public record TradeResponse(
	Long id,
	Long coinPrice,
	Long coinCount,
	Long usedMoney,
	TradeStatus tradeStatus,
	Long coinAccountId,
	LocalDateTime tradedTime
) {
	public static TradeResponse from(Trade trade) {
		return new TradeResponse(
			trade.getId(),
			trade.getCoinPrice(),
			trade.getCoinCount(),
			trade.getUsedMoney(),
			trade.getTradeStatus(),
			trade.getCoinAccountId(),
			trade.getTradedTime()
		);
	}
}
