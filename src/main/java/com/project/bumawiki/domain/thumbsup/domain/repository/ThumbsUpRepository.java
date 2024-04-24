package com.project.bumawiki.domain.thumbsup.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.user.domain.User;

public interface ThumbsUpRepository extends JpaRepository<ThumbsUp, Long> {

	Boolean existsByDocs_IdAndUser(Long docsId, User user);

	void deleteByDocsAndUser(Docs docs, User user);

	List<ThumbsUp> findByDocs_Id(Long docsId);

	long countByDocs_Title(String title);

}
