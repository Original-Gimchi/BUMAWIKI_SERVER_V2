package com.project.bumawiki.domain.coin.service;

import static com.project.bumawiki.global.util.RandomUtil.*;

import java.security.SecureRandom;
import java.util.List;

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

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {
	private static final Long CHANGE_MONEY_RANGE = 200000L;
	private final PriceReader priceReader;
	private final PriceCreator priceCreator;
	private final TradeReader tradeReader;
	private final TradeCreator tradeCreator;
	private final CoinAccountReader coinAccountReader;
	private final CoinAccountCreator coinAccountCreator;

	@Scheduled(fixedRate = 180000)
	void changePrice() {
		Price recentPrice = priceReader.getRecentPrice();
		long max = recentPrice.getPrice() + CHANGE_MONEY_RANGE;
		long min = Math.max(recentPrice.getPrice() - CHANGE_MONEY_RANGE, 0L);

		SecureRandom random = getRandomInstance();
		long randomPrice = random.nextLong(max - min + 1L) + min;
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

		for (Trade sellingTrade : sellingTrades) {
			if (sellingTrade.getCoinPrice() <= newPrice.getPrice()) {
				CoinAccount tradingAccount = coinAccountReader.getById(sellingTrade.getCoinAccountId());

				tradingAccount.sellCoin(sellingTrade.getCoinPrice(), sellingTrade.getCoinCount());
				sellingTrade.updateTradeStatus(TradeStatus.SOLD);
				tradeCreator.create(sellingTrade);
			}
		}
	}

	private void processBuyingTrade(Price newPrice) {
		List<Trade> buyingTrades = tradeReader.findAllByTradeStatus(TradeStatus.BUYING);

		for (Trade buyingTrade : buyingTrades) {
			if (buyingTrade.getCoinPrice() >= newPrice.getPrice()) {
				CoinAccount tradingAccount = coinAccountReader.getById(buyingTrade.getCoinAccountId());

				tradingAccount.buyCoin(buyingTrade.getCoinPrice(), buyingTrade.getCoinCount());
				buyingTrade.updateTradeStatus(TradeStatus.BOUGHT);
				tradeCreator.create(buyingTrade);
			}
		}
	}

}
