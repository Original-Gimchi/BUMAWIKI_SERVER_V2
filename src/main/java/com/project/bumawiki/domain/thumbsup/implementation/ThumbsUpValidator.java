package com.project.bumawiki.domain.thumbsup.implementation;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class ThumbsUpValidator {
	private final ThumbsUpReader thumbsUpReader;

	public void checkAlreadyThumbsUp(Docs docs, User user) {
		if (thumbsUpReader.checkDocsLike(docs.getId(), user)) {
			throw new BumawikiException(ErrorCode.ALREADY_THUMBS_UP);
		}
	}

	public void checkNotThumbsUp(Docs docs, User user) {
		if (!thumbsUpReader.checkDocsLike(docs.getId(), user)) {
			throw new BumawikiException(ErrorCode.YOU_DONT_THUMBS_UP_THIS_DOCS);
		}
	}
}
