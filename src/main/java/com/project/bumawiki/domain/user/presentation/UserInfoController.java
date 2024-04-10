package com.project.bumawiki.domain.user.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.service.DocsInformationService;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.presentation.dto.UserResponseDto;
import com.project.bumawiki.domain.user.service.UserInfoService;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

	private final UserInfoService userInfoService;
	private final DocsInformationService docsInformationService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public UserResponseDto findUserInfo() {
		User user = SecurityUtil.getCurrentUserWithLogin();
		return new UserResponseDto(user, docsInformationService.findAllVersionDocsByUser(user));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDto> findAnotherUserInFo(@PathVariable Long id) {
		User foundUser = userInfoService.findAnotherInfo(id);
		UserResponseDto response = new UserResponseDto(foundUser,
			docsInformationService.findAllVersionDocsByUser(foundUser));
		return ResponseEntity.ok().body(response);
	}

}
