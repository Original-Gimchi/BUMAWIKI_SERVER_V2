package com.project.bumawiki.domain.docs.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.user.domain.User;

public interface VersionDocsRepository extends JpaRepository<VersionDocs, Long> {

	@NonNull
	VersionDocs findFirstByDocsOrderByVersionDesc(Docs docs);

	List<VersionDocs> findTop2ByDocsOrderByVersionDesc(Docs docs);

	List<VersionDocs> findByDocs(Docs docs);

	Optional<VersionDocs> findByDocsAndVersion(Docs docs, Integer version);

	List<VersionDocs> findAllByUser(User user);

}
