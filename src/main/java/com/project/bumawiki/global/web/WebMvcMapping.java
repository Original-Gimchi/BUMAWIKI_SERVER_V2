package com.project.bumawiki.global.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.bumawiki.domain.auth.implementation.AuthReader;
import com.project.bumawiki.domain.auth.implementation.AuthUpdater;
import com.project.bumawiki.domain.auth.interceptor.AuthInterceptor;
import com.project.bumawiki.domain.auth.util.JwtParser;
import com.project.bumawiki.domain.user.implementation.UserReader;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcMapping implements WebMvcConfigurer {
	private final JwtParser jwtParser;
	private final AuthUpdater authUpdater;
	private final AuthReader authReader;
	private final UserReader userReader;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(
				"http://localhost:3000",
				"http://localhost:3001",
				"http://localhost:3002",
				"https://buma.wiki"
			)
			.allowedMethods("*")
			.allowedHeaders("*");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor(jwtParser, authUpdater, authReader, userReader));
	}
}
