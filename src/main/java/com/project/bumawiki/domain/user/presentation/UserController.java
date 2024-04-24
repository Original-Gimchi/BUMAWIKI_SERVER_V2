package com.project.bumawiki.domain.user.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.AdminOnly;
import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.docs.service.QueryDocsService;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.presentation.dto.UserAuthorityRequestDto;
import com.project.bumawiki.domain.user.presentation.dto.UserResponseDto;
import com.project.bumawiki.domain.user.service.CommandUserService;
import com.project.bumawiki.domain.user.service.QueryUserService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final QueryUserService userInfoService;
	private final QueryDocsService queryDocsService;
	private final QueryAuthService queryAuthService;
	private final CommandUserService commandUserService;

	@GetMapping
	@LoginRequired
	public UserResponseDto findMyInfo() {
		User user = queryAuthService.getCurrentUser();
		return new UserResponseDto(user, queryDocsService.findAllVersionDocsByUser(user));
	}

	@GetMapping("/{id}")
	public UserResponseDto findAnotherUserInFo(@PathVariable Long id) {
		User foundUser = userInfoService.findUserInfo(id);
		return new UserResponseDto(
			foundUser,
			queryDocsService.findAllVersionDocsByUser(foundUser)
		);
	}

	@AdminOnly
	@PatchMapping("/authority")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateUserAuthority(@RequestBody UserAuthorityRequestDto userAuthorityRequestDto) {
		commandUserService.updateUserAuthority(userAuthorityRequestDto.id(), userAuthorityRequestDto.authority());
	}

}
