package com.project.bumawiki.global.config.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.domain.user.exception.UserNotFoundException;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ServiceWithTransactionalReadOnly
public class AuthDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
			.map(AuthDetails::new)
			.orElseThrow(() -> UserNotFoundException.EXCEPTION);
	}
}
