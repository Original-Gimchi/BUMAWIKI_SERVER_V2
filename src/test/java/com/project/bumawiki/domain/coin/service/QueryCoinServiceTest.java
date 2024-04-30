package com.project.bumawiki.domain.coin.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.TradeWithoutTradeStatusAndCoinAccountId;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
import com.project.bumawiki.domain.coin.presentation.dto.RankingResponse;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

class QueryCoinServiceTest extends ServiceTest {
	@Autowired
	private QueryCoinService queryCoinService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CoinAccountRepository coinAccountRepository;

	@Autowired
	private TradeRepository tradeRepository;

	@Nested
	class 코인_계정_찾기 {
		@RepeatedTest(REPEAT_COUNT)
		void 유저_정보로_코인_계정_찾기() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long money = FixtureGenerator.getDefaultLongArbitrary()
				.greaterOrEqual(0L)
				.sample();

			CoinAccount coinAccount = new CoinAccount(user.getId(), money);

			coinAccountRepository.save(coinAccount);

			// when
			CoinAccount findCoinAccount = queryCoinService.findCoinAccountByUser(user);

			// then
			assertThat(findCoinAccount.getUserId()).isEqualTo(user.getId());
		}

		@RepeatedTest(REPEAT_COUNT)
		void 코인_저장이_안되어있을때() {
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> queryCoinService.findCoinAccountByUser(user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COIN_ACCOUNT_NOT_FOUND_EXCEPTION);
		}
	}

	@RepeatedTest(REPEAT_COUNT)
	void 코인_계정으로_거래_내역_조회하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		CoinAccount coinAccount = new CoinAccount(
			user.getId(),
			10000000L
		);

		coinAccountRepository.save(coinAccount);

		TradeWithoutTradeStatusAndCoinAccountId coinData =
			new TradeWithoutTradeStatusAndCoinAccountId(1000000L, 3L);

		Trade trade = coinData.toTrade(coinAccount);

		coinAccount.buyCoin(trade.getCoinPrice(), trade.getCoinCount());
		trade.updateTradeStatus(TradeStatus.BOUGHT);

		tradeRepository.save(trade);

		// when
		List<Trade> trades = queryCoinService.getTrades(coinAccount.getId());

		// then
		assertAll(
			() -> assertThat(trades.size()).isEqualTo(1),
			() -> assertThat(trades.get(0).getTradeStatus()).isEqualTo(TradeStatus.BOUGHT),
			() -> assertThat(trades.get(0).getCoinAccountId()).isEqualTo(coinAccount.getId())
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 기간_별로_코인_가격_조회하기() {
		// given
		Arbitrary<String> periods = Arbitraries.of("full", "halfMonth", "week", "day", "halfDay", "threeHours");

		// when
		List<Price> prices = queryCoinService.getPriceByPeriod(periods.sample());

		// then
		assertAll(
			() -> assertThat(prices.get(0)).isNotNull(),
			() -> assertThat(prices.size()).isEqualTo(2)
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 랭킹_조회하기() {
		// given
		List<User> users = FixtureGenerator.getDefaultUserBuilder()
			.sampleList(10);

		userRepository.saveAll(users);

		users.forEach(user -> {
			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample()
			);

			coinAccountRepository.save(coinAccount);
		});

		// when
		List<RankingResponse> rankingResponses =
			queryCoinService.getRanking(PageRequest.of(0, 10));

		// then
		assertThat(rankingResponses.size()).isEqualTo(10);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 현재_코인_시세_조회하기() {
		// when
		Price price = queryCoinService.getRecentPrice();

		// then
		assertThat(price.getPrice()).isNotNull();
	}
}
