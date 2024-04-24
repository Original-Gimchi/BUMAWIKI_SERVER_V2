package com.project.bumawiki.domain.coin.implementation;

import com.project.bumawiki.domain.coin.domain.Price;
import com.project.bumawiki.domain.coin.domain.repository.PriceRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class PriceCreator {
	private final PriceRepository priceRepository;

	public Price create(Price price) {
		return priceRepository.save(price);
	}
}
