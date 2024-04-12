package com.project.bumawiki.global.config.jwt;

import static io.jsonwebtoken.security.Keys.*;
import static java.nio.charset.StandardCharsets.*;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt")
public record JwtCredentials(
	SecretKey secretKey,
	long accessTokenExpirationTime,
	long refreshTokenExpirationTime
) {

	@ConstructorBinding
	public JwtCredentials(String secret, long accessExp, long refreshExp) {
		this(
			hmacShaKeyFor(secret.getBytes(UTF_8)),
			accessExp,
			refreshExp
		);
	}
}
