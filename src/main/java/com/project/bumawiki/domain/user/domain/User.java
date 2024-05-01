package com.project.bumawiki.domain.user.domain;

import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	public void update(User user) {
		this.email = user.getEmail();
		this.name = user.getName();
		this.enroll = user.getEnroll();
		this.nickName = user.getNickName();
	}

	public void updateAuthority(Authority authority) {
		this.authority = authority;
	}

}
