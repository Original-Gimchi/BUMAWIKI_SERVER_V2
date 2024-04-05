package com.project.bumawiki.domain.user.presentation.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.contribute.dto.ContributeResponseDto;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserResponseDto {
	private final Long id;

	private final String email;

	private final String nickName;

	private final String name;

	private final Authority authority;

	private final List<ContributeResponseDto> contributeDocs;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.nickName = user.getNickName();
		this.authority = user.getAuthority();
		this.name = user.getName();
		List<Contribute> contributeDocs = getContributeReversed(user);
		this.contributeDocs = contributeDocs
			.stream()
			.map(ContributeResponseDto::new)
			.collect(Collectors.toList());
	}

	@NotNull
	private static List<Contribute> getContributeReversed(User user) {
		List<Contribute> contributeDocs = user.getContributeDocs();
		Collections.reverse(contributeDocs);
		return contributeDocs;
	}
}
