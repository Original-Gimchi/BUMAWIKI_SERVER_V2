package com.project.bumawiki.global.error;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

public class ExceptionFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (BumawikiException e) {
			writeErrorCode(response, e.getErrorCode());
		} catch (ExpiredJwtException e) {
			writeErrorCode(response, ErrorCode.EXPIRED_JWT);
		} catch (JwtException e) {
			writeErrorCode(response, ErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
			e.printStackTrace();
			writeErrorCode(response, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private void writeErrorCode(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		ErrorResponse errorResponse = new ErrorResponse(
			errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()
		);

		response.setStatus(errorResponse.getStatus());
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(errorResponse.toString());
	}
}
