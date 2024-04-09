package com.project.bumawiki.domain.contribute.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.user.domain.User;

public interface ContributeRepository extends JpaRepository<Contribute, Long> {
	@Query("select distinct c.contributor from Contribute c where c.docs = :docs")
	List<User> findUserAllByDocs(Docs docs);
}
