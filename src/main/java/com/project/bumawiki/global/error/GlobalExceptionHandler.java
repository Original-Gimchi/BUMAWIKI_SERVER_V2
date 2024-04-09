package com.project.bumawiki.global.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	private static final String errorLogsFormat = """
		{
			"status": "%s",
			"code": "%s",
			"message": "%s"
		}
		""";

	@ExceptionHandler(BumawikiException.class)
	public ResponseEntity<ErrorResponse> handleGlobal(BumawikiException error) {
		final ErrorCode errorCode = error.getErrorCode();
		log.error(
			errorLogsFormat.formatted(
				errorCode.getStatus(),
				errorCode.getCode(),
				errorCode.getMessage()
			)
		);
		return new ResponseEntity<>(
			new ErrorResponse(
				errorCode.getStatus(),
				errorCode.getCode(),
				errorCode.getMessage()),
			HttpStatus.valueOf(errorCode.getStatus())
		);
	}

	@ExceptionHandler({BindException.class})
	public ResponseEntity<?> bindException(BindException bindException) {
		Map<String, String> errorMap = new HashMap<>();

		for (FieldError error : bindException.getFieldErrors()) {
			errorMap.put(error.getField(), error.getDefaultMessage());
			log.error(error.toString());
			log.error(error.getDefaultMessage());
		}
		return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<?> constraintViolationException(ConstraintViolationException error) {
		Map<String, String> errorMap = new HashMap<>();

		for (ConstraintViolation<?> e : error.getConstraintViolations()) {
			errorMap.put("constraint error", e.getMessage());
			log.error(error.toString());
		}

		return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
		Map<String, String> errorMap = new HashMap<>();

		String errorMessage = error.getBindingResult()
			.getAllErrors()
			.get(0)
			.getDefaultMessage();

		errorMap.put("validation error", errorMessage);

		log.error(errorMessage);
		return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	}
}
