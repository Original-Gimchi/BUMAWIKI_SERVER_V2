package com.project.bumawiki.domain.coin.implementation;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.domain.coin.domain.repository.CoinAccountRepository;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class CoinAccountReader {

	private final CoinAccountRepository coinAccountRepository;

	public boolean existsByUserId(Long userId) {
		return coinAccountRepository.existsByUserId(userId);
	}

	public CoinAccount getById(Long id) {
		return coinAccountRepository.findById(id)
			.orElseThrow(() -> new BumawikiException(ErrorCode.COIN_ACCOUNT_NOT_FOUND_EXCEPTION));
	}

	public CoinAccount getByUserId(Long userId) {
		return coinAccountRepository.getByUserId(userId);
	}

	public List<CoinAccount> getRanking(Pageable pageable, Long price) {
		return coinAccountRepository.getRanking(pageable, price);
	}

	public List<CoinAccount> findAllByCoinGreaterThan0() {
		return coinAccountRepository.findAllByCoinGreaterThan0();
	}
}
