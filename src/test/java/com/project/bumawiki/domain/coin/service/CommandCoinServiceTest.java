package com.project.bumawiki.domain.coin.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.TradeWithoutTradeStatusAndCoinAccountId;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

class CommandCoinServiceTest extends ServiceTest {
	@Autowired
	private CommandCoinService commandCoinService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CoinAccountRepository coinAccountRepository;

	@Autowired
	private TradeRepository tradeRepository;

	@RepeatedTest(REPEAT_COUNT)
	void 코인_계정_생성하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		// when
		commandCoinService.createCoinAccount(user);

		// then
		assertAll(
			() -> {
				assertThat(coinAccountRepository.findByUserId(user.getId()).isPresent()).isEqualTo(true);
				assertThat(coinAccountRepository.findByUserId(user.getId()).get().getUserId()).isEqualTo(user.getId());
			}
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 코인_구매하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		TradeWithoutTradeStatusAndCoinAccountId coinData = FixtureGenerator
			.getRandomTradeWithoutTradeStatusAndCoinAccountId();

		CoinAccount coinAccount = new CoinAccount(
			user.getId(),
			CommandCoinService.FIRST_MONEY
		);

		coinAccountRepository.save(coinAccount);

		// when
		commandCoinService.buyCoin(coinData, user);

		// then
		Long accountId = Objects.requireNonNull(coinAccountRepository.findByUserId(user.getId()).orElse(null)).getId();
		assertAll(
			() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
			() -> assertThat(
				tradeRepository.findAllByCoinAccountIdOrderByTradedTimeDesc(accountId).get(0).getCoinPrice()
			).isEqualTo(coinData.getCoinPrice()),
			() -> assertThat(
				tradeRepository.findAllByCoinAccountIdOrderByTradedTimeDesc(accountId).get(0).getCoinCount()
			).isEqualTo(coinData.getCoinCount())
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 코인_판매하기() {

	}
}
