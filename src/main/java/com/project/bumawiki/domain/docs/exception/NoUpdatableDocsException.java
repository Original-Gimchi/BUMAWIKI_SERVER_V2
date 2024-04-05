package com.project.bumawiki.domain.docs.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class NoUpdatableDocsException extends BumawikiException {

	public static final NoUpdatableDocsException EXCEPTION = new NoUpdatableDocsException(ErrorCode.NO_UPDATABLE_DOCS);

	public NoUpdatableDocsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
