package com.project.bumawiki.global.jwt.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtConstants {

	AUTH_ID("auth_id"),
	TYPE("type"),
	EMPTY(" "),
	PREFIX("prefix"),
	ROLE("role"),
	ACCESS_KEY("access_token"),
	REFRESH_KEY("refresh_token");

	public final String message;
}
