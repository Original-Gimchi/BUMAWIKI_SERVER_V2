package com.project.bumawiki.domain.coin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.implementation.CoinAccountReader;
import com.project.bumawiki.domain.coin.implementation.PriceReader;
import com.project.bumawiki.domain.coin.implementation.TradeReader;
import com.project.bumawiki.domain.coin.presentation.dto.RankingResponse;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.implementation.UserReader;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryCoinService {

	private final CoinAccountReader coinAccountReader;
	private final TradeReader tradeReader;
	private final PriceReader priceReader;
	private final UserReader userReader;

	public CoinAccount findCoinAccountByUser(User currentUserWithLogin) {
		return coinAccountReader.getByUserId(currentUserWithLogin.getId());
	}

	public List<Trade> getTrades(Long accountId) {
		return tradeReader.findAllByCoinAccountIdOrderByTradedTimeDesc(accountId);
	}

	public List<Price> getPriceByPeriod(String period) {
		switch (period) {
			case "full" -> {
				return priceReader.findAllByOrderByStartedTime();
			}
			case "halfMonth" -> {
				LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
				return priceReader.findAllAfterStartedTime(twoWeeksAgo);
			}
			case "week" -> {
				LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
				return priceReader.findAllAfterStartedTime(oneWeekAgo);
			}
			case "day" -> {
				LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
				return priceReader.findAllAfterStartedTime(oneDayAgo);
			}
			case "halfDay" -> {
				LocalDateTime halfDayAgo = LocalDateTime.now().minusHours(12);
				return priceReader.findAllAfterStartedTime(halfDayAgo);
			}
			case "threeHours" -> {
				LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
				return priceReader.findAllAfterStartedTime(threeHoursAgo);
			}
		}

		throw new BumawikiException(ErrorCode.NO_PERIOD);
	}

	public List<RankingResponse> getRanking(Pageable pageable) {
		Price recentPrice = getRecentPrice();
		return coinAccountReader.getRanking(pageable, recentPrice.getPrice())
			.stream()
			.map(ranking -> new RankingResponse(
					ranking,
					recentPrice.getPrice(),
					userReader.getById(ranking.getUserId())
				)
			).toList();
	}

	public Price getRecentPrice() {
		return priceReader.getRecentPrice();
	}
}
