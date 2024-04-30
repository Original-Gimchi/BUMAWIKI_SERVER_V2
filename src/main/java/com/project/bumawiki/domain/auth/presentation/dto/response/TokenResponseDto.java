package com.project.bumawiki.domain.auth.presentation.dto.response;

import com.project.bumawiki.domain.auth.domain.Token;

public record TokenResponseDto(
	String accessToken,
	String refreshToken
) {
	public static TokenResponseDto from(Token token) {
		return new TokenResponseDto(
			token.accessToken(),
			token.refreshToken()
		);
	}
}
