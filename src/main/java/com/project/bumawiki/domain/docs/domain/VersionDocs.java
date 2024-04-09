package com.project.bumawiki.domain.docs.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@IdClass(VersionDocsPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VersionDocs {

	@Id
	private int version;

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


}
