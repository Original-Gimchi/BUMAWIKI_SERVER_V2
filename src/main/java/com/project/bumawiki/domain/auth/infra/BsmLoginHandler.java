package com.project.bumawiki.domain.auth.infra;

import java.io.IOException;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class BsmLoginHandler {
	private final BsmOauth bsmOauth;

	public User getUserByAuthId(String authId) {
		try {
			String token = bsmOauth.getToken(authId);
			BsmResourceResponse response = bsmOauth.getResource(token);
			return createUnknownUser(response);
		} catch (BsmAuthCodeNotFoundException | BsmAuthTokenNotFoundException e) {
			throw new BumawikiException(ErrorCode.INVALID_AUTHID);
		} catch (BsmAuthInvalidClientException e) {
			throw new BumawikiException(ErrorCode.INVALID_BSM_CLIENT);
		} catch (IOException e) {
			throw new BumawikiException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private User createUnknownUser(BsmResourceResponse resource) {
		return User.builder()
			.email(resource.getEmail())
			.nickName(resource.getNickname())
			.authority(Authority.USER)
			.enroll(resource.getStudent().getEnrolledAt())
			.name(resource.getStudent().getName())
			.build();
	}
}
