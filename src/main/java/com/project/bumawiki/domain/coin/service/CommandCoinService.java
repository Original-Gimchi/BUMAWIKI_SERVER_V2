package com.project.bumawiki.domain.coin.service;

import static com.project.bumawiki.global.util.RandomUtil.*;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.TradeVo;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
import com.project.bumawiki.domain.coin.implementation.CoinAccountCreator;
import com.project.bumawiki.domain.coin.implementation.CoinAccountReader;
import com.project.bumawiki.domain.coin.implementation.CoinAccountValidator;
import com.project.bumawiki.domain.coin.implementation.PriceReader;
import com.project.bumawiki.domain.coin.implementation.TradeCreator;
import com.project.bumawiki.domain.coin.implementation.TradeReader;
import com.project.bumawiki.domain.coin.implementation.TradeUpdater;
import com.project.bumawiki.domain.coin.implementation.TradeValidator;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandCoinService {
	public static final Long FIRST_MONEY = 10000000L;
	private final CoinAccountReader coinAccountReader;
	private final CoinAccountCreator coinAccountCreator;
	private final CoinAccountValidator coinAccountValidator;
	private final PriceReader priceReader;
	private final TradeReader tradeReader;
	private final TradeCreator tradeCreator;
	private final TradeUpdater tradeUpdater;
	private final TradeValidator tradeValidator;

	public CoinAccount createCoinAccount(User user) {
		coinAccountValidator.checkAlreadyCreatedAccount(user.getId());

		CoinAccount coinAccount = new CoinAccount(
			user.getId(),
			FIRST_MONEY
		);

		return coinAccountCreator.create(coinAccount);
	}

	public Trade buyCoin(TradeVo coinData, User user) {
		CoinAccount coinAccount = coinAccountReader.getByUserId(user.getId());
		Price nowPrice = priceReader.getRecentPrice();

		Trade trade = coinData.toTrade(coinAccount);

		if (coinData.getCoinPrice() >= nowPrice.getPrice()) {
			buyNow(trade, coinAccount);
		} else {
			buyLater(trade);
		}

		return tradeCreator.create(trade);
	}

	private void buyLater(Trade trade) {
		tradeUpdater.updateTradeStatus(trade, TradeStatus.BUYING);
	}

	private void buyNow(Trade trade, CoinAccount coinAccount) {
		coinAccount.buyCoin(trade.getCoinPrice(), trade.getCoinCount());
		tradeUpdater.updateTradeStatus(trade, TradeStatus.BOUGHT);
	}

	public Trade sellCoin(TradeVo coinData, User user) {
		CoinAccount coinAccount = coinAccountReader.getByUserId(user.getId());
		Price nowPrice = priceReader.getRecentPrice();

		Trade trade = coinData.toTrade(coinAccount);

		if (coinData.getCoinPrice() <= nowPrice.getPrice()) {
			sellNow(trade, coinAccount);
		} else {
			sellLater(trade);
		}

		return tradeCreator.create(trade);
	}

	private void sellLater(Trade trade) {
		tradeUpdater.updateTradeStatus(trade, TradeStatus.SELLING);
	}

	private void sellNow(Trade trade, CoinAccount coinAccount) {
		coinAccount.sellCoin(trade.getCoinPrice(), trade.getCoinCount());
		tradeUpdater.updateTradeStatus(trade, TradeStatus.SOLD);
	}

	public void cancelTrade(Long tradeId, User user) {
		Trade trade = tradeReader.findById(tradeId);

		tradeValidator.checkTradeStatusIsNotBuyingAndSelling(trade.getTradeStatus());

		CoinAccount byId = coinAccountReader.getById(trade.getCoinAccountId());

		tradeValidator.checkTradeOwnership(byId.getUserId(), user.getId());

		trade.updateTradeStatus(TradeStatus.CANCELLED);
	}

	public Long dailyCheck(User user) {
		long randomNumber = getRandomNumber();

		CoinAccount account = coinAccountReader.getByUserId(user.getId());

		coinAccountValidator.checkAlreadyRewardedToday(account);

		account.addMoney(randomNumber);
		account.updateLastRewardedTimeNow();

		return randomNumber;
	}

	private long getRandomNumber() {
		long min = 50000;
		long max = 200000;

		SecureRandom random = getRandomInstance();

		long randomNumber = random
			.longs(min, max)
			.findFirst()
			.orElseThrow(() -> new BumawikiException(ErrorCode.INTERNAL_SERVER_ERROR));
		randomNumber -= randomNumber % 10000;

		return randomNumber;
	}
}
