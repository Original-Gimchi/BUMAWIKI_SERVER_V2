package com.project.bumawiki.domain.auth.domain;

public record Token(
	String accessToken,
	String refreshToken
) {
}
