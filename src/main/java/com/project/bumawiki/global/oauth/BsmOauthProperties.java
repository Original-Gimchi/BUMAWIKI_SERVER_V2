package com.project.bumawiki.global.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import leehj050211.bsmOauth.BsmOauth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "bsm")
public class BsmOauthProperties {

	private final String clientId;
	private final String secretKey;

	@Bean("bsmOauth")
	public BsmOauth bsmOauth() {
		return new BsmOauth(clientId, secretKey);
	}
}
