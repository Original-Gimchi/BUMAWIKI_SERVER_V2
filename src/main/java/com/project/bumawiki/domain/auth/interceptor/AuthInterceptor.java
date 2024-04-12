package com.project.bumawiki.domain.auth.interceptor;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.project.bumawiki.domain.auth.annotation.AdminOnly;
import com.project.bumawiki.domain.auth.annotation.LoginOrNot;
import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.implementation.AuthReader;
import com.project.bumawiki.domain.auth.service.implementation.AuthUpdater;
import com.project.bumawiki.domain.auth.util.BearerTokenExtractor;
import com.project.bumawiki.domain.auth.util.JwtParser;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
	private final JwtParser jwtParser;
	private final AuthUpdater authUpdater;
	private final AuthReader authReader;
	//TODO UserReader로 변경
	private final UserRepository userRepository;

	private static void shouldUserAdmin(User currentUser) {
		if (currentUser.getAuthority() != Authority.ADMIN) {
			throw new BumawikiException(ErrorCode.USER_NOT_ADMIN);
		}
	}

	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull Object handler
	) {
		if (handler instanceof HandlerMethod hm) {
			if (hm.hasMethodAnnotation(LoginOrNot.class)) {
				String bearer = request.getHeader(AUTHORIZATION);

				if (bearer == null) {
					authUpdater.updateCurrentUser(null);
				} else {
					String jwt = BearerTokenExtractor.extract(bearer);
					Long userId = jwtParser.getIdFromJwt(jwt);

					User user = userRepository.getById(userId);

					authUpdater.updateCurrentUser(user);
				}
			}

			if (hm.hasMethodAnnotation(LoginRequired.class)) {
				if (authReader.getCurrentUser() == null) {
					throw new BumawikiException(ErrorCode.USER_NOT_LOGIN);
				}
			}
			if (hm.hasMethodAnnotation(AdminOnly.class)) {
				User currentUser = authReader.getCurrentUser();
				shouldUserAdmin(currentUser);
			}
		}
		return true;
	}
}
