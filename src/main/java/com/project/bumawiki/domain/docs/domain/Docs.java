package com.project.bumawiki.domain.docs.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.LastModifiedDate;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.domain.type.Status;
import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsup.exception.AlreadyThumbsUpException;
import com.project.bumawiki.domain.thumbsup.exception.YouDontThumbsUpThisDocs;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.user.domain.User;

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
public class Docs {

	@OneToMany(
		mappedBy = "docs",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@Builder.Default
	private final List<ThumbsUp> thumbsUps = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "docs_id")
	private Long id;
	@Column(length = 32, unique = true)
	private String title;
	@Column(length = 8)
	private int enroll;
	@Enumerated(EnumType.STRING)
	private DocsType docsType;
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VersionDocs> docsVersion = new ArrayList<>();
	@Builder.Default
	@OneToMany(mappedBy = "docs", cascade = CascadeType.ALL)
	private List<Contribute> contributor = new ArrayList<>();
	private int lastVersion;
	@Enumerated(EnumType.STRING)
	private Status status;

	public void cancelThumbsUp(ThumbsUp thumbsUp) {
		boolean removed = thumbsUps
			.removeIf(thumbsUp::equals);

		if (!removed) {
			throw YouDontThumbsUpThisDocs.EXCEPTION;
		}
	}

	public boolean doesUserThumbsUp(User user) {
		return thumbsUps
			.stream()
			.anyMatch(like -> like.doesUserThumbsUp(user));
	}

	public void addThumbsUp(ThumbsUp thumbsUp) {
		boolean anyMatch = thumbsUps
			.stream()
			.anyMatch(iterThumbsUp -> iterThumbsUp.equals(thumbsUp));

		if (anyMatch) {
			throw AlreadyThumbsUpException.EXCEPTION;
		}
		this.thumbsUps.add(thumbsUp);
	}

	public List<ThumbsUpResponseDto> getList() {
		return this.thumbsUps
			.stream()
			.map(ThumbsUp::getDto)
			.collect(Collectors.toList());
	}

	public void updateDocsType(DocsType docsType) {
		this.docsType = docsType;
	}

	public void setVersionDocs(List<VersionDocs> versionDocs) {
		this.docsVersion = versionDocs;
	}

	public void updateContributor(List<Contribute> contributes) {
		this.contributor = contributes;
	}

	public void setModifiedTime(LocalDateTime lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public boolean doesUserLike(User user) {
		return doesUserThumbsUp(user);
	}

	public int getThumbsUpsCount() {
		if (thumbsUps == null) {
			return 0;
		}
		return thumbsUps.size();
	}

	public void updateLatestVersion(int version) {
		this.lastVersion = version;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}
}
