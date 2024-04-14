package com.project.bumawiki.domain.auth.presentation;

import java.io.IOException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.presentation.dto.LoginReqestDto;
import com.project.bumawiki.domain.auth.presentation.dto.RefreshTokenRequestDto;
import com.project.bumawiki.domain.auth.presentation.dto.TokenResponseDto;
import com.project.bumawiki.domain.auth.service.CommandAuthService;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final CommandAuthService commandAuthService;

	@PostMapping("/oauth/bsm")
	public TokenResponseDto userSignup(@RequestBody LoginReqestDto loginReqestDto) throws IOException {
		return TokenResponseDto.from(commandAuthService.login(loginReqestDto.accessToken()));
	}

	@PutMapping("/refresh/access")
	public TokenResponseDto refreshAccessToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
		return TokenResponseDto.from(commandAuthService.refresh(refreshTokenRequestDto.refreshToken()));
	}
}
