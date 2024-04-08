package com.project.bumawiki.domain.docs.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class CannotChangeYourDocsException extends BumawikiException {

	public static final CannotChangeYourDocsException EXCEPTION = new CannotChangeYourDocsException(
		ErrorCode.CANNOT_CHANGE_YOUR_DOCS);

	public CannotChangeYourDocsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
