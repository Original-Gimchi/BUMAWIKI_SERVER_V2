package com.project.bumawiki.domain.coin.implementation;

import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class TradeCreator {
	private final TradeRepository tradeRepository;

	public Trade create(Trade trade) {
		return tradeRepository.save(trade);
	}
}
