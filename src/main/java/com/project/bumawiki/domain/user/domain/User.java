package com.project.bumawiki.domain.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.thumbsUp.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsUp.exception.AlreadyThumbsUpexception;
import com.project.bumawiki.domain.thumbsUp.exception.YouDontThumbsUpThisDocs;
import com.project.bumawiki.domain.thumbsUp.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import leehj050211.bsmOauth.dto.resource.BsmUserResource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(unique = true, length = 32)
	private String email;

	@Column(length = 16)
	private String name;

	@Column(length = 8)
	private Integer enroll;

	@Column(length = 20)
	private String nickName;

	@Enumerated(EnumType.STRING)
	@Column(length = 16)
	private Authority authority;

	@Builder.Default
	@OneToMany(mappedBy = "contributor", cascade = CascadeType.ALL)
	private List<Contribute> contributeDocs = new ArrayList<>();

	@OneToMany(
		mappedBy = "user",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@Builder.Default
	private final List<ThumbsUp> thumbsUps = new ArrayList<>();

	public void cancelThumbsUp(ThumbsUp thumbsUp) {
		boolean removed = thumbsUps
			.removeIf(thumbsUp::equals);

		if (!removed) {
			throw YouDontThumbsUpThisDocs.EXCEPTION;
		}
	}

	public void addThumbsUp(ThumbsUp thumbsUp) {
		boolean anyMatch = thumbsUps
			.stream()
			.anyMatch(iterThumbsUp -> iterThumbsUp.equals(thumbsUp));

		if (anyMatch) {
			throw AlreadyThumbsUpexception.EXCEPTION;
		}
		this.thumbsUps.add(thumbsUp);
	}

	public List<ThumbsUpResponseDto> getList() {
		return this.thumbsUps
			.stream()
			.map(ThumbsUp::getDto)
			.collect(Collectors.toList());
	}

	public User update(BsmUserResource resource) {
		this.email = resource.getEmail();
		this.name = resource.getStudent().getName();
		this.enroll = resource.getStudent().getEnrolledAt();
		this.nickName = resource.getNickname();
		return this;
	}

	public void changeUserAuthority(Authority authority) {
		this.authority = authority;
	}

	public void updateContributeDocs(List<Contribute> contribute) {
		this.contributeDocs = contribute;
	}
}
