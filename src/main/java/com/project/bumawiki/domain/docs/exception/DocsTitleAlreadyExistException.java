package com.project.bumawiki.domain.docs.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class DocsTitleAlreadyExistException extends BumawikiException {

	public static final DocsTitleAlreadyExistException EXCEPTION = new DocsTitleAlreadyExistException(
		ErrorCode.POST_TITLE_ALREADY_EXIST);

	public DocsTitleAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}
}
