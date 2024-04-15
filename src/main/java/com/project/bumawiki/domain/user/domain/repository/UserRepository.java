package com.project.bumawiki.domain.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bumawiki.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
