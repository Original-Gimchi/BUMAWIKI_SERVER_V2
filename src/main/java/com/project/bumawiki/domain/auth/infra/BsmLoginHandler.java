package com.project.bumawiki.domain.auth.infra;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BsmLoginHandler {
	private final BsmOauth bsmOauth;

	public BsmResourceResponse getResource(String authId) {
		try {
			String token = bsmOauth.getToken(authId);
			return bsmOauth.getResource(token);
		} catch (BsmAuthCodeNotFoundException | BsmAuthTokenNotFoundException | BsmAuthInvalidClientException e) {
			throw new BumawikiException(ErrorCode.USER_NOT_LOGIN);
		} catch (IOException e) {
			throw new BumawikiException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
