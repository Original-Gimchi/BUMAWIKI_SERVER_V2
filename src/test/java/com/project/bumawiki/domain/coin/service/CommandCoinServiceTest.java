package com.project.bumawiki.domain.coin.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.TradeWithoutTradeStatusAndCoinAccountId;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.domain.coin.domain.repository.PriceRepository;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
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

	@Autowired
	private PriceRepository priceRepository;

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

	@Nested
	class 코인_구매하기 {
		@RepeatedTest(REPEAT_COUNT)
		void 잔고가_충분하지_않을_경우() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(1000000L)
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, coinData.getCoinCount() * coinData.getCoinPrice() - 200000L)
					.sample()
			);

			coinAccountRepository.save(coinAccount);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.buyCoin(coinData, user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MONEY_NOT_ENOUGH);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 구매_가격이_시세보다_높거나_같을_때() {
			// given
			Price price = priceRepository.getRecentPrice();

			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(price.getPrice())
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(coinData.getCoinCount() * coinData.getCoinPrice())
					.sample()
			);

			coinAccountRepository.save(coinAccount);

			Long moneyBeforeBuyCoin = coinAccount.getMoney();

			// when
			commandCoinService.buyCoin(coinData, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
				() -> assertThat(
					moneyBeforeBuyCoin >= coinData.getCoinPrice() * coinData.getCoinCount()
				).isEqualTo(true),
				() -> assertThat(
					coinData.getCoinPrice() >= price.getPrice()
				).isEqualTo(true)
			);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 구매_가격이_시세보다_낮을_때() {
			// given
			Price price = priceRepository.getRecentPrice();

			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, price.getPrice() - 1L)
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(coinData.getCoinCount() * coinData.getCoinPrice())
					.sample()
			);

			coinAccountRepository.save(coinAccount);

			Long moneyBeforeBuyCoin = coinAccount.getMoney();

			// when
			commandCoinService.buyCoin(coinData, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
				() -> assertThat(
					moneyBeforeBuyCoin >= coinData.getCoinPrice() * coinData.getCoinCount()
				).isEqualTo(true),
				() -> assertThat(
					coinData.getCoinPrice() < price.getPrice()
				).isEqualTo(true)
			);
		}
	}

	@Nested
	class 코인_판매하기 {
		@RepeatedTest(REPEAT_COUNT)
		void 잔고가_충분하지_않을_경우() {
			// given
			Price price = priceRepository.getRecentPrice();

			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(1000000L)
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, coinData.getCoinCount() * coinData.getCoinPrice() - 200000L)
					.sample()
			);

			coinAccountRepository.save(coinAccount);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.buyCoin(coinData, user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MONEY_NOT_ENOUGH);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 구매_가격이_시세보다_높거나_같을_때() {
			// given
			Price price = priceRepository.getRecentPrice();

			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(price.getPrice())
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(coinData.getCoinCount() * coinData.getCoinPrice())
					.sample()
			);

			coinAccountRepository.save(coinAccount);

			Long moneyBeforeBuyCoin = coinAccount.getMoney();

			// when
			commandCoinService.buyCoin(coinData, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
				() -> assertThat(
					moneyBeforeBuyCoin >= coinData.getCoinPrice() * coinData.getCoinCount()
				).isEqualTo(true),
				() -> assertThat(
					coinData.getCoinPrice() >= price.getPrice()
				).isEqualTo(true)
			);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 구매_가격이_시세보다_낮을_때() {
			// given
			Price price = priceRepository.getRecentPrice();

			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, price.getPrice() - 1L)
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(coinData.getCoinCount() * coinData.getCoinPrice())
					.sample()
			);

			coinAccountRepository.save(coinAccount);

			Long moneyBeforeBuyCoin = coinAccount.getMoney();

			// when
			commandCoinService.buyCoin(coinData, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
				() -> assertThat(
					moneyBeforeBuyCoin >= coinData.getCoinPrice() * coinData.getCoinCount()
				).isEqualTo(true),
				() -> assertThat(
					coinData.getCoinPrice() < price.getPrice()
				).isEqualTo(true)
			);
		}
	}
}
