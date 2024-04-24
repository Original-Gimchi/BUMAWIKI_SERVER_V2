package com.project.bumawiki.domain.user.domain;

import java.util.ArrayList;
import java.util.List;

import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
	private Long id;

	@OneToMany(
		mappedBy = "user",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@Builder.Default
	private final List<ThumbsUp> thumbsUps = new ArrayList<>();
	@Email
	@Size(max = 32)
	@Column(unique = true, length = 32)
	private String email;

	@Column(length = 8)
	private Integer enroll;

	@Size(max = 20)
	@Column(length = 20)
	private String nickName;

	@Column(length = 16)
	@Enumerated(EnumType.STRING)
	private Authority authority;
	@NotNull
	@Size(max = 16)
	@Column(length = 16)
	private String name;

	public List<ThumbsUpResponseDto> getList() {
		return this.thumbsUps
			.stream()
			.map(ThumbsUp::getDto)
			.toList();
	}

	public void update(User user) {
		this.email = user.getEmail();
		this.name = user.getName();
		this.enroll = user.getEnroll();
		this.nickName = user.getNickName();
	}

	public void changeUserAuthority(Authority authority) {
		this.authority = authority;
	}
}
