package com.project.bumawiki.domain.docs.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.project.bumawiki.domain.docs.domain.type.DocsType;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Docs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 32)
	@Column(length = 32, unique = true)
	private String title;

	@Column
	@NotNull
	private Integer enroll;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DocsType docsType;

	private LocalDateTime lastModifiedAt;

	@OneToMany(mappedBy = "docs", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<VersionDocs> versionDocs;

	public Docs(String title, int enroll, DocsType docsType) {
		this.title = title;
		this.enroll = enroll;
		this.docsType = docsType;
	}

	public void updateDocsType(DocsType docsType) {
		this.docsType = docsType;
	}

	public void updateModifiedAt(LocalDateTime lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public void updateTitle(String title) {
		this.title = title;
	}
}
