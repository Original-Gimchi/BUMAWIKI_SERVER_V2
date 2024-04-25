package com.project.bumawiki.domain.coin.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;
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
		@Nested
		class 구매_가격이_시세보다_높거나_같을_때 {
			@RepeatedTest(REPEAT_COUNT)
			void 잔고가_충분할_경우() {
				// given
				Price price = priceRepository.getRecentPrice();
				System.out.println(price.getPrice());

				User user = FixtureGenerator.getDefaultUserBuilder().sample();

				userRepository.save(user);

				TradeWithoutTradeStatusAndCoinAccountId coinData = FixtureGenerator
					.getRandomTradeWithoutTradeStatusAndCoinAccountId()
					.setPostCondition(
						javaGetter(TradeWithoutTradeStatusAndCoinAccountId::getCoinPrice),
						Long.class,
						it -> it < CommandCoinService.FIRST_MONEY / 100L
					)
					.set(javaGetter(TradeWithoutTradeStatusAndCoinAccountId::getCoinCount), price.getPrice()/100000L)
					.sample();

				CoinAccount coinAccount = new CoinAccount(
					user.getId(),
					CommandCoinService.FIRST_MONEY
				);

				coinAccountRepository.save(coinAccount);

				// when
				commandCoinService.buyCoin(coinData, user);

				// then
				assertAll(
					() -> assertThat(tradeRepository.findAll().size()).isEqualTo(1),
					() -> assertThat(
						coinAccount.getMoney() > coinData.getCoinPrice() * coinData.getCoinCount()
					).isEqualTo(true)
				);
			}

			// @RepeatedTest(REPEAT_COUNT)
			// void 잔고가_충분하지_않을_경우() {
			//
			// }
		}
	}

	@RepeatedTest(REPEAT_COUNT)
	void 코인_판매하기() {

	}
}
