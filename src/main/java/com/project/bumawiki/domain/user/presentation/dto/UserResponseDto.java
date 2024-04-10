package com.project.bumawiki.domain.user.presentation.dto;

import java.util.List;

import com.project.bumawiki.domain.contribute.dto.ContributeResponseDto;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;

import lombok.Getter;

@Getter
public class UserResponseDto {
	private final Long id;

	private final String email;

	private final String nickName;

	private final String name;

	private final Authority authority;

	private final List<ContributeResponseDto> contributeDocs;

	public UserResponseDto(User user, List<VersionDocs> versionDocs) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.nickName = user.getNickName();
		this.authority = user.getAuthority();
		this.name = user.getName();
		this.contributeDocs = versionDocs
			.stream()
			.map(ContributeResponseDto::new)
			.toList();
	}
}
