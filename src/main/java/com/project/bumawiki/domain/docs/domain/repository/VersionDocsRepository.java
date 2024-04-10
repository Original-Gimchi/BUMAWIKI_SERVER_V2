package com.project.bumawiki.domain.docs.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;

public interface VersionDocsRepository extends JpaRepository<VersionDocs, Long> {

	@NonNull
	VersionDocs findFirstByDocsOrderByVersionDesc(Docs docs);

	List<VersionDocs> findTop3ByDocsOrderByVersion(Docs docs);

	List<VersionDocs> findByDocs(Docs docs);
}
