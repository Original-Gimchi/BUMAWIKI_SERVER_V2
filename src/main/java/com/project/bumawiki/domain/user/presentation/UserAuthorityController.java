package com.project.bumawiki.domain.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.presentation.dto.UserAuthorityDto;
import com.project.bumawiki.domain.user.service.UserAuthorityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserAuthorityController {
	private final UserAuthorityService userAuthorityService;

	@PutMapping("/set/authority")
	public ResponseEntity<Authority> setUserAuthority(@RequestBody final UserAuthorityDto userAuthorityDto) {
		Authority authority = userAuthorityService.execute(userAuthorityDto);
		return ResponseEntity.ok().body(authority);
	}

}
