package com.project.bumawiki.domain.user.implementation;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

import leehj050211.bsmOauth.dto.response.BsmResourceResponse;

@Implementation
public class UserUpdater {
	public void update(User user, BsmResourceResponse resource) {
		user.update(resource);
	}
}
