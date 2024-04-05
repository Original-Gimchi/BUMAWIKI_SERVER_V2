package com.project.bumawiki.domain.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.project.bumawiki.domain.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Optional<RefreshToken> findById(String authId);

	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
