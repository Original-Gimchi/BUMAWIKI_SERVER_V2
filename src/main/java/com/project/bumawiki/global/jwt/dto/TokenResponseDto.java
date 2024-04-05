package com.project.bumawiki.global.jwt.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponseDto {
	private final String accessToken;
	private final String refreshToken;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final ZonedDateTime expiredAt;

	@Builder
	public TokenResponseDto(String accessToken, String refreshToken, ZonedDateTime expiredAt) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiredAt = expiredAt;
	}
}
