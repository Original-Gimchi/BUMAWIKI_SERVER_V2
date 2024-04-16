package com.project.bumawiki.domain.docs.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public interface DocsRepository extends JpaRepository<Docs, Long> {

	@Query("select d from Docs d where d.docsType = :docsType order by d.enroll")
	List<Docs> findByDocsType(@Param("docsType") DocsType docsType);

	@Query("select d from Docs d where d.title like CONCAT('%',:title,'%') order by d.lastModifiedAt desc")
	List<Docs> findAllByTitle(@Param("title") String title);

	@Query("select d from Docs d where d.title like :title")
	Optional<Docs> findByTitle(@Param("title") String title);

	@Query("select d from Docs d order by d.lastModifiedAt desc")
	List<Docs> findByLastModifiedAt(Pageable pageable);

	@Query("select d from Docs d order by d.lastModifiedAt desc")
	List<Docs> findByLastModifiedAtAll();

	boolean existsByTitle(String title);

	default Docs getByTitle(String title) {
		return findByTitle(title)
			.orElseThrow(() -> new BumawikiException(ErrorCode.DOCS_NOT_FOUND));
	}
}
