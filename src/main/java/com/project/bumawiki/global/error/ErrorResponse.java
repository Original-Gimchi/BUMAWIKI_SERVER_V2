package com.project.bumawiki.global.error;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	@JsonIgnore
	private static final String errorLogsFormat = """
		{
			"status": "%s",
			"code": "%s",
			"message": "%s"
		}
		""";

	private int status;
	private String code;
	private String message;

	public String toString() {
		return errorLogsFormat.formatted(
			status,
			code,
			message
		);
	}
}
