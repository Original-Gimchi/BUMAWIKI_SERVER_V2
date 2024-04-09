package com.project.bumawiki.domain.auth.presentation;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.service.AccessTokenRefreshService;
import com.project.bumawiki.domain.user.service.UserLoginService;
import com.project.bumawiki.domain.user.service.UserLogoutService;
import com.project.bumawiki.global.jwt.dto.TokenResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UserLoginService userLoginService;
	private final UserLogoutService userLogoutService;
	private final AccessTokenRefreshService accessTokenRefreshService;

	@PostMapping("/oauth/bsm")
	public TokenResponseDto userSignup(@RequestHeader("authCode") String authCode) throws IOException {
		return ResponseEntity.ok(userLoginService.execute(authCode)).getBody();
	}

	@DeleteMapping("/bsm/logout")
	public ResponseEntity<String> userLogout(@RequestHeader("refreshToken") String refreshToken) {
		return ResponseEntity.ok(userLogoutService.execute(refreshToken));
	}

	@PutMapping("/refresh/access")
	public TokenResponseDto refreshAccessToken(@RequestHeader("refreshToken") String refreshToken) {
		return ResponseEntity.ok(accessTokenRefreshService.execute(refreshToken)).getBody();
	}
}
