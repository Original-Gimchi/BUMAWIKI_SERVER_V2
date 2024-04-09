package com.project.bumawiki.domain.docs.domain;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.domain.type.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Docs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 32, unique = true)
	private String title;

	@Column
	private int enroll;

	@Enumerated(EnumType.STRING)
	private DocsType docsType;

	private LocalDateTime lastModifiedAt;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Docs(String title, int enroll, DocsType docsType, Status status) {
		this.title = title;
		this.enroll = enroll;
		this.docsType = docsType;
		this.status = status;
	}

	public void updateDocsType(DocsType docsType) {
		this.docsType = docsType;
	}

	public void setModifiedTime(LocalDateTime lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}
}
