package com.project.bumawiki.domain.coin.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bumawiki.domain.coin.domain.CoinAccount;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public interface CoinAccountRepository extends JpaRepository<CoinAccount, Long> {
	Optional<CoinAccount> findByUserId(Long userId);

	boolean existsByUserId(Long userId);

	default CoinAccount getByUserId(Long userId) {
		return findByUserId(userId)
			.orElseThrow(() -> new BumawikiException(ErrorCode.COIN_ACCOUNT_NOT_FOUND_EXCEPTION));
	}

	default CoinAccount getById(Long id) {
		return findById(id)
			.orElseThrow(() -> new BumawikiException(ErrorCode.COIN_ACCOUNT_NOT_FOUND_EXCEPTION));
	}

	@Query(value = "select c from CoinAccount c where c.coin > 0")
	List<CoinAccount> findAllByCoinGreaterThan0();

	@Query(value = "select c from CoinAccount c order by c.money + c.coin * :price desc, c.gotMoney asc")
	List<CoinAccount> getRanking(Pageable pageable, Long price);
}
