package com.project.bumawiki.domain.thumbsup.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.user.domain.User;

public interface ThumbsUpRepository extends JpaRepository<ThumbsUp, Long> {

	Boolean existsByDocs_IdAndUser(Long docs_id, User user);
}
