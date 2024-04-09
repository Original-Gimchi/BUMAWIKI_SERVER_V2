package com.project.bumawiki.domain.coin.domain;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.coin.exception.CoinNotEnoughException;
import com.project.bumawiki.domain.coin.exception.MoneyNotEnoughException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoinAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private Long money;
	private Long coin;
	private LocalDateTime lastRewardedTime;
	private Long gotMoney;

	public CoinAccount(Long userId, Long money) {
		this.userId = userId;
		this.money = money;
		this.gotMoney = money;
		this.coin = 0L;
		lastRewardedTime = LocalDateTime.of(2006, 7, 4, 0, 0);
	}

	public void buyCoin(Long coinPrice, Long coinCount) {
		if (this.money < coinPrice * coinCount) {
			throw new MoneyNotEnoughException();
		}
		this.money -= coinPrice * coinCount;
		this.coin += coinCount;
	}

	public void sellCoin(Long coinPrice, Long coinCount) {
		if (this.coin < coinCount) {
			throw new CoinNotEnoughException();
		}
		this.coin -= coinCount;
		this.money += coinPrice * coinCount;
	}

	public void addMoney(Long money) {
		this.money += money;
		this.gotMoney += money;
	}

	public boolean wasRewardedToday() {
		LocalDateTime today = LocalDateTime.now();
		return this.lastRewardedTime.getYear() == today.getYear()
			&& this.lastRewardedTime.getMonth() == today.getMonth()
			&& this.lastRewardedTime.getDayOfMonth() == today.getDayOfMonth();
	}

	public void updateLastRewardedTimeNow() {
		this.lastRewardedTime = LocalDateTime.now();
	}
}
