package com.project.bumawiki.domain.auth.presentation;

import java.io.IOException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.presentation.dto.TokenResponseDto;
import com.project.bumawiki.domain.auth.service.AccessTokenRefreshService;
import com.project.bumawiki.domain.auth.service.UserSignUpOrUpdateService;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UserSignUpOrUpdateService userSignUpOrUpdateService;
	private final AccessTokenRefreshService accessTokenRefreshService;

	@PostMapping("/oauth/bsm")
	public TokenResponseDto userSignup(@RequestHeader("authCode") String authCode) throws IOException {
		return TokenResponseDto.from(userSignUpOrUpdateService.execute(authCode));
	}

	@PutMapping("/refresh/access")
	public TokenResponseDto refreshAccessToken(@RequestHeader("refreshToken") String refreshToken) {
		return TokenResponseDto.from(accessTokenRefreshService.execute(refreshToken));
	}
}
