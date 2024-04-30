package com.project.bumawiki.domain.coin.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.TradeWithoutTradeStatusAndCoinAccountId;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.domain.coin.domain.repository.PriceRepository;
import com.project.bumawiki.domain.coin.domain.repository.TradeRepository;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;
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

	@Nested
	class 코인_계정_생성하기 {
		@RepeatedTest(REPEAT_COUNT)
		void 코인_계정_생성할_때() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			// when
			commandCoinService.createCoinAccount(user);

			// then
			assertAll(
				() -> {
					assertTrue(coinAccountRepository.findByUserId(user.getId()).isPresent());
					assertThat(coinAccountRepository.findByUserId(user.getId()).get().getUserId())
						.isEqualTo(user.getId());
				}
			);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 이미_계정이_존재할_때() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			commandCoinService.createCoinAccount(user);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.createCoinAccount(user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_CREATED);
		}
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
					.greaterOrEqual(1L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(1200000L)
					.sample();
			}

			TradeWithoutTradeStatusAndCoinAccountId coinData =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount);

			System.out.println(coinData.getCoinCount());
			System.out.println(coinData.getCoinPrice());

			CoinAccount coinAccount = new CoinAccount(user.getId(), 0L);

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
					.greaterOrEqual(1200000L)
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
			Trade trade = commandCoinService.buyCoin(coinData, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
				() -> assertThat(
					moneyBeforeBuyCoin >= coinData.getCoinPrice() * coinData.getCoinCount()
				).isEqualTo(true),
				() -> assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.BOUGHT),
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
					.between(0L, 800000L)
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
			Trade trade = commandCoinService.buyCoin(coinData, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
				() -> assertThat(
					moneyBeforeBuyCoin >= coinData.getCoinPrice() * coinData.getCoinCount()
				).isEqualTo(true),
				() -> assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.BUYING),
				() -> assertThat(
					coinData.getCoinPrice() < price.getPrice()
				).isEqualTo(true)
			);
		}
	}

	@Nested
	class 코인_판매하기 {
		@RepeatedTest(REPEAT_COUNT)
		void 코인이_충분하지_않을_경우() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, Long.MAX_VALUE)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, 1000000L)
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

			commandCoinService.buyCoin(coinData, user);

			TradeWithoutTradeStatusAndCoinAccountId coinDataToSell =
				new TradeWithoutTradeStatusAndCoinAccountId(coinPrice, coinCount + 1);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.sellCoin(coinDataToSell, user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COIN_NOT_ENOUGH);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 구매_가격이_시세보다_낮거나_같을_때() {
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
					.between(0L, 800000L)
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

			commandCoinService.buyCoin(coinData, user);

			TradeWithoutTradeStatusAndCoinAccountId coinDataToSell =
				new TradeWithoutTradeStatusAndCoinAccountId(
					coinPrice,
					FixtureGenerator.getDefaultLongArbitrary()
						.between(0L, coinAccount.getCoin())
						.sample()
				);

			// when
			Trade trade = commandCoinService.sellCoin(coinDataToSell, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(2),
				() -> assertThat(
					coinAccount.getCoin() < coinDataToSell.getCoinCount()
				).isEqualTo(false),
				() -> assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.SOLD),
				() -> assertThat(
					coinData.getCoinPrice() <= price.getPrice()
				).isEqualTo(true)
			);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 구매_가격이_시세보다_높을_때() {
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
					.greaterOrEqual(price.getPrice() + 200000L)
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

			commandCoinService.buyCoin(coinData, user);

			TradeWithoutTradeStatusAndCoinAccountId coinDataToSell =
				new TradeWithoutTradeStatusAndCoinAccountId(
					coinPrice,
					FixtureGenerator.getDefaultLongArbitrary()
						.between(0L, coinAccount.getCoin())
						.sample()
				);

			// when
			Trade trade = commandCoinService.sellCoin(coinDataToSell, user);

			// then
			assertAll(
				() -> assertThat(tradeRepository.findAll().size()).isEqualTo(2),
				() -> assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.SELLING),
				() -> assertThat(
					coinData.getCoinPrice() > price.getPrice()
				).isEqualTo(true)
			);
		}
	}

	@Nested
	class 거래_취소하기 {
		@RepeatedTest(REPEAT_COUNT)
		void 거래_취소할_때() {
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
					.between(0L, 800000L)
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

			Trade trade = commandCoinService.buyCoin(coinData, user);

			// when
			commandCoinService.cancelTrade(trade.getId(), user);

			// then
			assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.CANCELLED);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 이미_완료된_거래일_때() {
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
					.greaterOrEqual(1200000L)
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

			Trade trade = commandCoinService.buyCoin(coinData, user);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.cancelTrade(trade.getId(), user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.TRADE_ALREADY_FINISHED);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 다른_유저가_요청했을_때() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			User otherUser = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(otherUser);

			Long coinCount = 0L;
			Long coinPrice = 0L;

			while (coinCount * coinPrice <= 0) {
				coinCount = FixtureGenerator.getDefaultLongArbitrary()
					.greaterOrEqual(0L)
					.sample();
				coinPrice = FixtureGenerator.getDefaultLongArbitrary()
					.between(0L, 800000L)
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

			// when
			Trade trade = commandCoinService.buyCoin(coinData, user);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.cancelTrade(trade.getId(), otherUser)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANCEL_OTHERS_TRADE);
		}
	}

	@Nested
	class 일일_보상_받기 {
		@RepeatedTest(REPEAT_COUNT)
		void 일일_보상_받을_때() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				CommandCoinService.FIRST_MONEY
			);

			coinAccountRepository.save(coinAccount);

			// when
			commandCoinService.dailyCheck(user);

			CoinAccount updatedCoinAccount = coinAccountRepository.getByUserId(user.getId());

			// then
			assertAll(
				() -> assertThat(
					updatedCoinAccount.wasRewardedToday()
				).isTrue(),
				() -> assertThat(
					updatedCoinAccount.getMoney() - CommandCoinService.FIRST_MONEY
				).isBetween(50000L, 200000L)
			);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 이미_보상을_받았을_때() {
			// given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();

			userRepository.save(user);

			CoinAccount coinAccount = new CoinAccount(
				user.getId(),
				CommandCoinService.FIRST_MONEY
			);

			coinAccountRepository.save(coinAccount);

			commandCoinService.dailyCheck(user);

			// when
			BumawikiException exception = assertThrows(
				BumawikiException.class,
				() -> commandCoinService.dailyCheck(user)
			);

			// then
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_AWARDED);
		}
	}
}
