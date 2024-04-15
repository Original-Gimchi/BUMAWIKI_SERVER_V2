package com.project.bumawiki.domain.user.implementation;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

@Implementation
public class UserUpdater {
	public void update(User user, User newUserInfo) {
		user.update(newUserInfo);
	}
}
