package com.project.bumawiki.domain.coin.implementation;

import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class TradeValidator {

	public void checkTradeStatusIsNotBuyingAndSelling(TradeStatus tradeStatus) {
		if (tradeStatus != TradeStatus.BUYING && tradeStatus != TradeStatus.SELLING) {
			throw new BumawikiException(ErrorCode.TRADE_ALREADY_FINISHED);
		}
	}

	public void checkTradeOwnership(Long coinAccountUserId, Long userId) {
		if (!coinAccountUserId.equals(userId)) {
			throw new BumawikiException(ErrorCode.CANCEL_OTHERS_TRADE);
		}
	}
}
