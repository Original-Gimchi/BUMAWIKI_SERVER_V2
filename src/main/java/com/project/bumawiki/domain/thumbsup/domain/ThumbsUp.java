package com.project.bumawiki.domain.thumbsup.domain;

import java.util.Objects;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThumbsUp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "thumbs_up_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "docs_id")
	private Docs docs;

	public boolean doesUserThumbsUp(User user) {
		return this.user.equals(user);
	}

	public boolean doYouThumbsUp(Docs docs) {
		return this.docs.equals(docs);
	}

	public boolean equals(ThumbsUp thumbsUp) {
		return Objects.equals(user, thumbsUp.getUser())
			&& Objects.equals(docs, thumbsUp.docs);
	}

	public ThumbsUpResponseDto getDto() {
		return new ThumbsUpResponseDto(docs);
	}
}
