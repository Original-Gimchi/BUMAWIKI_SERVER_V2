package com.project.bumawiki.domain.coin.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.coin.presentation.dto.CoinAccountResponse;
import com.project.bumawiki.domain.coin.presentation.dto.TradeRequest;
import com.project.bumawiki.domain.coin.presentation.dto.TradeResponse;
import com.project.bumawiki.domain.coin.service.CommandCoinService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coins")
public class CommandCoinController {

	private final CommandCoinService commandCoinService;
	private final QueryAuthService queryAuthService;

	@PostMapping
	@LoginRequired
	public CoinAccountResponse createCoinAccount() {
		return CoinAccountResponse.from(
			commandCoinService.createCoinAccount(queryAuthService.getCurrentUser())
		);
	}

	@PostMapping("/buy")
	@LoginRequired
	public TradeResponse buyCoin(@Valid @RequestBody TradeRequest tradeRequest) {
		return TradeResponse.from(
			commandCoinService.buyCoin(tradeRequest.toEntity(), queryAuthService.getCurrentUser())
		);
	}

	@PostMapping("/sell")
	@LoginRequired
	public TradeResponse sellCoin(@Valid @RequestBody TradeRequest tradeRequest) {
		return TradeResponse.from(
			commandCoinService.sellCoin(tradeRequest.toEntity(), queryAuthService.getCurrentUser())
		);
	}

	@PostMapping("/daily")
	@LoginRequired
	public Long dailyCheck() {
		return commandCoinService.dailyCheck(queryAuthService.getCurrentUser());
	}

	@DeleteMapping("/{tradeId}")
	@LoginRequired
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void cancelTrade(@PathVariable Long tradeId) {
		commandCoinService.cancelTrade(tradeId, queryAuthService.getCurrentUser());
	}
}
