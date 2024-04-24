package com.project.bumawiki.domain.coin.implementation;

import java.util.List;

import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class TradeReader {
	private final TradeRepository tradeRepository;

	public Trade findById(Long tradeId) {
		return tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BumawikiException(ErrorCode.TRADE_NOT_FOUND));
	}

	public List<Trade> findAllByCoinAccountIdOrderByTradedTimeDesc(Long accountId) {
		return tradeRepository.findAllByCoinAccountIdOrderByTradedTimeDesc(accountId);
	}
}
