package com.project.bumawiki.domain.coin.service;

import static com.project.bumawiki.global.util.RandomUtil.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
import com.project.bumawiki.domain.coin.implementation.CoinAccountCreator;
import com.project.bumawiki.domain.coin.implementation.CoinAccountReader;
import com.project.bumawiki.domain.coin.implementation.PriceCreator;
import com.project.bumawiki.domain.coin.implementation.PriceReader;
import com.project.bumawiki.domain.coin.implementation.TradeCreator;
import com.project.bumawiki.domain.coin.implementation.TradeReader;
import com.project.bumawiki.domain.coin.implementation.TradeUpdater;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {
	private static final Long CHANGE_MONEY_RANGE = 200000L;
	private final PriceReader priceReader;
	private final PriceCreator priceCreator;
	private final TradeReader tradeReader;
	private final TradeCreator tradeCreator;
	private final TradeUpdater tradeUpdater;
	private final CoinAccountReader coinAccountReader;
	private final CoinAccountCreator coinAccountCreator;

	@Scheduled(fixedRate = 180000)
	void changePrice() {
		Price recentPrice = priceReader.getRecentPrice();
		long max = recentPrice.getPrice() + CHANGE_MONEY_RANGE;
		long min = Math.max(recentPrice.getPrice() - CHANGE_MONEY_RANGE, 0L);

		SecureRandom random = getRandomInstance();
		long randomPrice = random
			.longs(min, max)
			.findFirst()
			.orElseThrow(() -> new BumawikiException(ErrorCode.INTERNAL_SERVER_ERROR));
		Price newPrice;
		if (randomPrice == 0) {
			restartCoin();
			newPrice = new Price(1000000L);
		} else {
			newPrice = new Price(randomPrice - randomPrice % 100);
		}

		priceCreator.create(newPrice);
		processBuyingTrade(newPrice);
		processSellingTrade(newPrice);
	}

	private void restartCoin() {
		List<CoinAccount> coinAccounts = coinAccountReader.findAllByCoinGreaterThan0();

		for (CoinAccount coinAccount : coinAccounts) {
			Trade trade = new Trade(
				0L,
				coinAccount.getCoin(),
				0L,
				TradeStatus.DELISTING,
				coinAccount.getId()
			);
			coinAccount.sellCoin(0L, coinAccount.getCoin());

			tradeCreator.create(trade);
			coinAccountCreator.create(coinAccount);
		}
	}

	private void processSellingTrade(Price newPrice) {
		List<Trade> sellingTrades = tradeReader.findAllByTradeStatus(TradeStatus.SELLING);

		processTrade(
			sellingTrades,
			TradeStatus.SOLD,
			(Trade trade) -> trade.getCoinPrice() <= newPrice.getPrice()
		);
	}

	private void processBuyingTrade(Price newPrice) {
		List<Trade> buyingTrades = tradeReader.findAllByTradeStatus(TradeStatus.BUYING);

		processTrade(
			buyingTrades,
			TradeStatus.BOUGHT,
			(Trade trade) -> trade.getCoinPrice() >= newPrice.getPrice()
		);
	}

	private void processTrade(List<Trade> trades, TradeStatus tradeStatus, Predicate<Trade> p) {
		for (Trade trade : trades) {
			if (p.test(trade)) {
				CoinAccount tradingAccount = coinAccountReader.getById(trade.getCoinAccountId());

				tradingAccount.buyCoin(trade.getCoinPrice(), trade.getCoinCount());
				tradeUpdater.updateTradeStatus(trade, tradeStatus);
				tradeCreator.create(trade);
			}
		}
	}
}
