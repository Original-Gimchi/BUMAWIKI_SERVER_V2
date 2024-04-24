package com.project.bumawiki.domain.user.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import net.jqwik.api.Arbitraries;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

class CommandUserServiceTest extends ServiceTest {

	@Autowired
	private CommandUserService commandUserService;
	@Autowired
	private UserRepository userRepository;

	@RepeatedTest(REPEAT_COUNT)
	void 유저_권한_업데이트() {
		// given
		Authority authority = Arbitraries.of(Authority.class).sample();
		Authority otherAuthority = Arbitraries.of(Authority.class).filter(it -> it != authority).sample();
		User user = FixtureGenerator.getDefaultUserBuilder()
			.set(javaGetter(User::getAuthority), authority)
			.sample();

		userRepository.save(user);
		// when
		commandUserService.updateUserAuthority(user.getId(), otherAuthority);

		// then
		assertThat(userRepository.findById(user.getId()).get().getAuthority()).isEqualTo(otherAuthority);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 없는_유저_권한_업데이트() {
		// given
		Authority authority = Arbitraries.of(Authority.class).sample();

		Long id = Arbitraries.longs().greaterOrEqual(1).sample();
		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandUserService.updateUserAuthority(id, authority)
		);

		// then
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
	}

}
