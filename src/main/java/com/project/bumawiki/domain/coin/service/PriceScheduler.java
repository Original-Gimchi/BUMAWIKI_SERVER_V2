package com.project.bumawiki.domain.coin.service;

import static com.project.bumawiki.global.util.RandomUtil.*;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.domain.coin.domain.repository.PriceRepository;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {
	private static final Long CHANGE_MONEY_RANGE = 200000L;
	private final PriceRepository priceRepository;
	private final TradeRepository tradeRepository;
	private final CoinAccountRepository coinAccountRepository;

	@Scheduled(fixedRate = 180000)
	void changePrice() {
		Price recentPrice = priceRepository.getRecentPrice();
		Long max = recentPrice.getPrice() + CHANGE_MONEY_RANGE;
		Long min = Math.max(recentPrice.getPrice() - CHANGE_MONEY_RANGE, 0L);

		SecureRandom random = getRandomInstance();
		Long randomPrice = random.nextLong(max - min + 1L) + min;
		Price newPrice;
		if (randomPrice == 0) {
			restartCoin();
			newPrice = new Price(1000000L);
		} else {
			newPrice = new Price(randomPrice - randomPrice % 100);
		}

		priceRepository.save(newPrice);
		processBuyingTrade(newPrice);
		processSellingTrade(newPrice);
	}

	private void restartCoin() {
		List<CoinAccount> coinAccounts = coinAccountRepository.findAllByCoinGreaterThan0();

		for (CoinAccount coinAccount : coinAccounts) {
			Trade trade = new Trade(
				0L,
				coinAccount.getCoin(),
				0L,
				TradeStatus.DELISTING,
				coinAccount.getId()
			);
			coinAccount.sellCoin(0L, coinAccount.getCoin());

			tradeRepository.save(trade);
			coinAccountRepository.save(coinAccount);
		}
	}

	private void processSellingTrade(Price newPrice) {
		List<Trade> sellingTrades = tradeRepository.findAllByTradeStatus(TradeStatus.SELLING);

		for (Trade sellingTrade : sellingTrades) {
			if (sellingTrade.getCoinPrice() <= newPrice.getPrice()) {
				CoinAccount tradingAccount = coinAccountRepository.getById(sellingTrade.getCoinAccountId());

				tradingAccount.sellCoin(sellingTrade.getCoinPrice(), sellingTrade.getCoinCount());
				sellingTrade.updateTradeStatus(TradeStatus.SOLD);
				tradeRepository.save(sellingTrade);
			}
		}
	}

	private void processBuyingTrade(Price newPrice) {
		List<Trade> buyingTrades = tradeRepository.findAllByTradeStatus(TradeStatus.BUYING);

		for (Trade buyingTrade : buyingTrades) {
			if (buyingTrade.getCoinPrice() >= newPrice.getPrice()) {
				CoinAccount tradingAccount = coinAccountRepository.getById(buyingTrade.getCoinAccountId());

				tradingAccount.buyCoin(buyingTrade.getCoinPrice(), buyingTrade.getCoinCount());
				buyingTrade.updateTradeStatus(TradeStatus.BOUGHT);
				tradeRepository.save(buyingTrade);
			}
		}
	}

}
