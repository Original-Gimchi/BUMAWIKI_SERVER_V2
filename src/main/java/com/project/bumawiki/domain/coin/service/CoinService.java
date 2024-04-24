package com.project.bumawiki.domain.coin.service;

import static com.project.bumawiki.global.util.RandomUtil.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.TradeWithoutTradeStatusAndCoinAccountId;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.domain.coin.domain.repository.PriceRepository;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
import com.project.bumawiki.domain.coin.presentation.dto.RankingResponse;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CoinService {
	public static final Long FIRST_MONEY = 10000000L;
	private final CoinAccountRepository coinAccountRepository;
	private final PriceRepository priceRepository;
	private final TradeRepository tradeRepository;
	private final UserRepository userRepository;

	public CoinAccount createCoinAccount(User user) {
		boolean alreadyCreatedAccount = coinAccountRepository.existsByUserId(user.getId());

		if (alreadyCreatedAccount) {
			throw new BumawikiException(ErrorCode.ALREADY_CREATED);
		}
		CoinAccount coinAccount = new CoinAccount(
			user.getId(),
			FIRST_MONEY
		);

		return coinAccountRepository.save(coinAccount);
	}

	@Transactional(readOnly = true)
	public CoinAccount findCoinAccountByUser(User currentUserWithLogin) {
		return coinAccountRepository.getByUserId(currentUserWithLogin.getId());
	}

	public Trade buyCoin(TradeWithoutTradeStatusAndCoinAccountId coinData, User user) {
		CoinAccount coinAccount = coinAccountRepository.getByUserId(user.getId());
		Price nowPrice = priceRepository.getRecentPrice();

		Trade trade = coinData.toTrade(coinAccount);

		if (coinData.coinPrice() >= nowPrice.getPrice()) {
			buyNow(trade, coinAccount);
		} else {
			buyLater(trade);
		}

		return tradeRepository.save(trade);
	}

	@Transactional
	public Trade sellCoin(TradeWithoutTradeStatusAndCoinAccountId coinData, User user) {
		CoinAccount coinAccount = coinAccountRepository.getByUserId(user.getId());
		Price nowPrice = priceRepository.getRecentPrice();

		Trade trade = coinData.toTrade(coinAccount);

		if (coinData.coinPrice() <= nowPrice.getPrice()) {
			sellNow(trade, coinAccount);
		} else {
			sellLater(trade);
		}

		return tradeRepository.save(trade);
	}

	private void sellLater(Trade trade) {
		trade.updateTradeStatus(TradeStatus.SELLING);
	}

	private void sellNow(Trade trade, CoinAccount coinAccount) {
		coinAccount.sellCoin(trade.getCoinPrice(), trade.getCoinCount());
		trade.updateTradeStatus(TradeStatus.SOLD);
	}

	private void buyLater(Trade trade) {
		trade.updateTradeStatus(TradeStatus.BUYING);
	}

	private void buyNow(Trade trade, CoinAccount coinAccount) {
		coinAccount.buyCoin(trade.getCoinPrice(), trade.getCoinCount());
		trade.updateTradeStatus(TradeStatus.BOUGHT);
	}

	@Transactional(readOnly = true)
	public List<Trade> getTrades(Long accountId) {
		return tradeRepository.findAllByCoinAccountIdOrderByTradedTimeDesc(accountId);
	}

	public List<Price> getPriceByPeriod(String period) {
		if (period.equals("full")) {
			return priceRepository.findAllByOrderByStartedTime();
		}

		if (period.equals("halfMonth")) {
			LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
			return priceRepository.findAllAfterStartedTime(twoWeeksAgo);
		}

		if (period.equals("week")) {
			LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
			return priceRepository.findAllAfterStartedTime(oneWeekAgo);
		}

		if (period.equals("day")) {
			LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
			return priceRepository.findAllAfterStartedTime(oneDayAgo);
		}

		if (period.equals("halfDay")) {
			LocalDateTime halfDayAgo = LocalDateTime.now().minusHours(12);
			return priceRepository.findAllAfterStartedTime(halfDayAgo);
		}

		if (period.equals("threeHours")) {
			LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
			return priceRepository.findAllAfterStartedTime(threeHoursAgo);
		}

		throw new BumawikiException(ErrorCode.NO_PERIOD);
	}

	public void cancelTrade(Long tradeId, User user) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BumawikiException(ErrorCode.TRADE_NOT_FOUND));

		if (trade.getTradeStatus() != TradeStatus.BUYING && trade.getTradeStatus() != TradeStatus.SELLING) {
			throw new BumawikiException(ErrorCode.TRADE_ALREADY_FINISHED);
		}

		CoinAccount byId = coinAccountRepository.getById(trade.getCoinAccountId());

		if (!byId.getUserId().equals(user.getId())) {
			throw new BumawikiException(ErrorCode.CANCEL_OTHERS_TRADE);
		}

		trade.updateTradeStatus(TradeStatus.CANCELLED);
	}

	public Long dailyCheck(User user) {
		long min = 50000;
		long max = 200000;

		SecureRandom random = getRandomInstance();
		Long randomNumber = (random.nextLong(max - min + 1) + min);
		randomNumber -= randomNumber % 10000;

		CoinAccount account = coinAccountRepository.getByUserId(user.getId());

		if (account.wasRewardedToday()) {
			throw new BumawikiException(ErrorCode.ALREADY_AWARDED);
		}
		account.addMoney(randomNumber);
		account.updateLastRewardedTimeNow();

		return randomNumber;
	}

	public List<RankingResponse> getRanking(Pageable pageable) {
		Price recentPrice = priceRepository.getRecentPrice();
		return coinAccountRepository.getRanking(pageable, recentPrice.getPrice())
			.stream()
			.map(ranking -> new RankingResponse(
					ranking,
					recentPrice.getPrice(),
					userRepository.getById(ranking.getUserId())
				)
			).toList();
	}

	public Price getRecentPrice() {
		return priceRepository.getRecentPrice();
	}
}
