package com.project.bumawiki.domain.coin.implementation;

import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
import com.project.bumawiki.global.annotation.Implementation;

@Implementation
public class TradeUpdater {

	public void updateTradeStatus(Trade trade, TradeStatus tradeStatus) {
		trade.updateTradeStatus(tradeStatus);
	}
}
