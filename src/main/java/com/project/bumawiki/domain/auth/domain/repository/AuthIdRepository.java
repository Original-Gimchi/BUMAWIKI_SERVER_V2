package com.project.bumawiki.domain.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.project.bumawiki.domain.auth.domain.AuthId;

public interface AuthIdRepository extends CrudRepository<AuthId, String> {
	Optional<AuthId> findByAuthId(String authId);

}
