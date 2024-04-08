package com.project.bumawiki.global.error;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bumawiki.global.error.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		ErrorCode errorCode = ErrorCode.FORBIDDEN;
		String errorResponseJson = objectMapper.writeValueAsString(
			new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()));

		response.setStatus(errorCode.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(errorResponseJson);
	}
}
