package com.project.bumawiki.domain.coin.implementation;

import java.time.LocalDateTime;
import java.util.List;

import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.repository.PriceRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class PriceReader {

	private final PriceRepository priceRepository;

	public Price getRecentPrice() {
		return priceRepository.getRecentPrice();
	}

	public List<Price> findAllByOrderByStartedTime() {
		return priceRepository.findAllByOrderByStartedTime();
	}

	public List<Price> findAllAfterStartedTime(LocalDateTime localDateTime) {
		return priceRepository.findAllAfterStartedTime(localDateTime);
	}
}
