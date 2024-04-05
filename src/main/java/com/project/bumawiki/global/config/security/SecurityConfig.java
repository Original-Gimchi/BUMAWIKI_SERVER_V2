package com.project.bumawiki.global.config.security;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bumawiki.global.error.CustomAuthenticationEntryPoint;
import com.project.bumawiki.global.error.ExceptionFilter;
import com.project.bumawiki.global.jwt.auth.JwtAuth;
import com.project.bumawiki.global.jwt.auth.JwtFilter;
import com.project.bumawiki.global.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;
	private final JwtUtil jwtUtil;
	private final JwtAuth jwtAuth;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.sessionManagement(
				(sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.requestMatchers(POST, "/api/docs/create").hasAnyAuthority("USER", "ADMIN")
				.requestMatchers(PUT, "/api/docs/update/**").hasAnyAuthority("USER", "ADMIN")
				.requestMatchers(DELETE, "/api/docs/delete/**").hasAuthority("ADMIN")
				.requestMatchers(PUT, "/api/docs/update/title/**").hasAuthority("ADMIN")
				.requestMatchers(PUT, "/api/docs/update/docsType").hasAuthority("ADMIN")
				//thumbsUp
				.requestMatchers(POST, "/api/thumbs/up").hasAnyAuthority("USER", "ADMIN")
				.requestMatchers(DELETE, "/api/thumbs/up").hasAnyAuthority("USER", "ADMIN")
				.requestMatchers(GET, "/api/thumbs/up").hasAnyAuthority("USER", "ADMIN")
				.anyRequest().permitAll()
			)
			.exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(
				new CustomAuthenticationEntryPoint(objectMapper)))
			.addFilterBefore(new JwtFilter(jwtAuth, jwtUtil), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new ExceptionFilter(), JwtFilter.class);

		return http.build();
	}
}
