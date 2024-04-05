package com.project.bumawiki.domain.contribute.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Contribute {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contribute_id")
	private Long id;

	@CreatedDate
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User contributor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "docs_id")
	private Docs docs;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "version_docs_id")
	private VersionDocs versionDocs;
}
