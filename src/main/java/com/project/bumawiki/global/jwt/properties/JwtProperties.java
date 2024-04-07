package com.project.bumawiki.global.jwt.properties;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {
	private final String header;
	private final SecretKey secret;
	private final Long accessExp;
	private final Long refreshExp;
	private final String prefix;

	@ConstructorBinding
	public JwtProperties(String header, String secret, Long accessExp, Long refreshExp, String prefix) {
		this.header = header;
		this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.accessExp = accessExp;
		this.refreshExp = refreshExp;
		this.prefix = prefix;
	}
}
