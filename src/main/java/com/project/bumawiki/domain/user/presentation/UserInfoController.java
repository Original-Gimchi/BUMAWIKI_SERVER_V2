package com.project.bumawiki.domain.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.presentation.dto.UserResponseDto;
import com.project.bumawiki.domain.user.service.UserInfoService;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserInfoController {

	private final UserInfoService userInfoService;

	@GetMapping("/user")
	public ResponseEntity<UserResponseDto> findUserInfo() {
		User loginUser = userInfoService.getLoginUser();
		UserResponseDto response = new UserResponseDto(loginUser);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/user/id/{id}")
	public ResponseEntity<UserResponseDto> findAnotherUserInFo(@PathVariable Long id) {
		User foundUser = userInfoService.findAnotherInfo(id);
		UserResponseDto response = new UserResponseDto(foundUser);
		return ResponseEntity.ok().body(response);
	}

}
