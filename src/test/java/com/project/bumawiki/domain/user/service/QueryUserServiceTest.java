package com.project.bumawiki.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import net.jqwik.api.Arbitraries;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

class QueryUserServiceTest extends ServiceTest {

	@Autowired
	private QueryUserService queryUserService;
	@Autowired
	private UserRepository userRepository;

	@RepeatedTest(REPEAT_COUNT)
	void 다른유저_ID로_조회() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder()
			.sample();

		userRepository.save(user);
		// when
		User foundUser = queryUserService.findUserInfo(user.getId());

		// then
		assertThat(user.getId()).isEqualTo(foundUser.getId());
	}

	@RepeatedTest(REPEAT_COUNT)
	void 없는_유저_조회() {
		// given
		Long id = Arbitraries.longs().greaterOrEqual(1).sample();

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> queryUserService.findUserInfo(id)
		);

		// then
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
	}

}
