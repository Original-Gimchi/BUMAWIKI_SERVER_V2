package com.project.bumawiki.domain.docs.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.project.bumawiki.domain.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(VersionDocsPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VersionDocs {

	@Id
	private Integer version;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "docs_id")
	private Docs docs;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String contents;

	@CreatedDate
	private LocalDateTime thisVersionCreatedAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User contributor;

	public VersionDocs(int version, Docs docs, String contents, User contributor) {
		this.version = version;
		this.docs = docs;
		this.contents = contents;
		this.contributor = contributor;
	}
}
