package com.project.bumawiki.domain.docs.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bumawiki.domain.docs.domain.VersionDocs;

public interface VersionDocsRepository extends JpaRepository<VersionDocs, Long> {
}
