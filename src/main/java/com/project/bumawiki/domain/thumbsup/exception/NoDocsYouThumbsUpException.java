package com.project.bumawiki.domain.thumbsup.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class NoDocsYouThumbsUpException extends BumawikiException {

	public static final NoDocsYouThumbsUpException EXCEPTION = new NoDocsYouThumbsUpException(
		ErrorCode.NO_DOCS_YOU_THUMBS_UP);

	private NoDocsYouThumbsUpException(ErrorCode errorCode) {
		super(errorCode);
	}
}
