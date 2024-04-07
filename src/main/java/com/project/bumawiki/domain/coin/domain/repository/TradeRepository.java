package com.project.bumawiki.domain.coin.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bumawiki.domain.coin.domain.Trade;
import com.project.bumawiki.domain.coin.domain.type.TradeStatus;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	List<Trade> findAllByCoinAccountIdOrderByTradedTimeDesc(Long coinAccountId);

	List<Trade> findAllByTradeStatus(TradeStatus tradeStatus);
}
