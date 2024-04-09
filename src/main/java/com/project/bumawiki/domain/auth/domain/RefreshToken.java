package com.project.bumawiki.domain.auth.domain;

import java.time.ZonedDateTime;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash
public class RefreshToken {
	@Id
	private String id;
	@Indexed
	private String refreshToken;

	private String role;

	@TimeToLive
	private long ttl;

	private ZonedDateTime expiredAt;

	public RefreshToken update(final String refreshToken, final long ttl) {
		this.refreshToken = refreshToken;
		this.ttl = ttl;
		return this;
	}
}
