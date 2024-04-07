package com.project.bumawiki.domain.docs.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class DocsTypeNotFoundException extends BumawikiException {

	public static final DocsTypeNotFoundException EXCEPTION = new DocsTypeNotFoundException(
		ErrorCode.DOCS_TYPE_NOT_FOUND);

	public DocsTypeNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
