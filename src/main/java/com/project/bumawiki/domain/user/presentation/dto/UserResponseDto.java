package com.project.bumawiki.domain.user.presentation.dto;

import java.util.List;

import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.presentation.dto.response.ContributeDocsResponseDto;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;

public record UserResponseDto(
	Long id,
	String email,
	String nickName,
	String name,
	Authority authority,
	List<ContributeDocsResponseDto> contributeDocs
) {
	public UserResponseDto(User user, List<VersionDocs> versionDocs) {
		this(
			user.getId(),
			user.getEmail(),
			user.getNickName(),
			user.getName(),
			user.getAuthority(),
			versionDocs.stream()
				.map(ContributeDocsResponseDto::new)
				.toList()
		);
	}
}
