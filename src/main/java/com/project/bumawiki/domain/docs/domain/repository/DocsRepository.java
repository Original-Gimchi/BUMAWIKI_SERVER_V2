package com.project.bumawiki.domain.docs.domain.repository;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocsRepository extends JpaRepository<Docs, Long>, CustomDocsRepository {

    @Query("select d from Docs d where d.docsType = :docsType order by d.enroll")
    List<Docs> findByDocsType(@Param("docsType") DocsType docsType);

    @Query("select d from Docs d where d.title like CONCAT('%',:title,'%') order by d.lastModifiedAt desc")
    List<Docs> findAllByTitle(@Param("title") String title);

    @Query("select d from Docs d where d.title like :title")
    Optional<Docs> findByTitle(@Param("title") String title);

    @Query("select d from Docs d order by d.lastModifiedAt desc")
    Page<Docs> findByLastModifiedAt(Pageable pageable);

    @Query("select d from Docs d order by d.lastModifiedAt asc")
    Page<Docs> findByLastModifiedAtAsc(Pageable pageable);

    @Query("select d from Docs d order by d.lastModifiedAt desc")
    List<Docs> findByLastModifiedAtAll();
}
