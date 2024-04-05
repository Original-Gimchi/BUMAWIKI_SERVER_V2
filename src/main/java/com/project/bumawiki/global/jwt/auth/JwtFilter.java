package com.project.bumawiki.global.jwt.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.bumawiki.global.jwt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtAuth jwtAuth;
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = jwtUtil.resolveToken(request);
		setAuthenticationInSecurityContext(token);
		filterChain.doFilter(request, response);
	}

	private void setAuthenticationInSecurityContext(String token) {
		if (token != null) {
			Authentication authentication = jwtAuth.authentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}
}
