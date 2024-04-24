package com.project.bumawiki.domain.thumbsup.domain.repository;

import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.user.domain.User;

public interface CustomThumbsUpRepository {
	List<Docs> getUserThumbsUp(User user);
}
