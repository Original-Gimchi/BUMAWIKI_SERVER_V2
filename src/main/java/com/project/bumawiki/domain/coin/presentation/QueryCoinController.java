package com.project.bumawiki.domain.coin.presentation;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.coin.presentation.dto.CoinAccountResponse;
import com.project.bumawiki.domain.coin.presentation.dto.PriceResponse;
import com.project.bumawiki.domain.coin.presentation.dto.RankingResponse;
import com.project.bumawiki.domain.coin.presentation.dto.TradeResponse;
import com.project.bumawiki.domain.coin.service.QueryCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coins")
public class QueryCoinController {
	private final QueryCoinService queryCoinService;
	private final QueryAuthService queryAuthService;

	@GetMapping("/mine")
	@LoginRequired
	public CoinAccountResponse findOwnAccount() {
		return CoinAccountResponse.from(
			queryCoinService.findCoinAccountByUser(queryAuthService.getCurrentUser())
		);
	}

	@GetMapping("/trades/{accountId}")
	public List<TradeResponse> getTrades(@PathVariable Long accountId) {
		return queryCoinService.getTrades(accountId)
			.stream()
			.map(TradeResponse::from)
			.toList();
	}

	@GetMapping("/graph")
	public List<PriceResponse> getGraph(@RequestParam String period) {
		return queryCoinService.getPriceByPeriod(period)
			.stream()
			.map(PriceResponse::from)
			.toList();
	}

	@GetMapping("/ranking")
	public List<RankingResponse> getRanking(@PageableDefault Pageable pageable) {
		return queryCoinService.getRanking(pageable);
	}

	@GetMapping("/prices")
	public PriceResponse getRecentPrice() {
		return PriceResponse.from(queryCoinService.getRecentPrice());
	}
}
