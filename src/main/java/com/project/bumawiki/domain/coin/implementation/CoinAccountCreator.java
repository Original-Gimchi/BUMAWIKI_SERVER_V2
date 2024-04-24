package com.project.bumawiki.domain.coin.implementation;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class CoinAccountCreator {

	private final CoinAccountRepository coinAccountRepository;

	public CoinAccount create(CoinAccount coinAccount) {
		return coinAccountRepository.save(coinAccount);
	}
}
