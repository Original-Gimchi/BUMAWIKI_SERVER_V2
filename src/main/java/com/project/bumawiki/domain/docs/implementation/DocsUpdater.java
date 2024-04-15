package com.project.bumawiki.domain.docs.implementation;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.global.annotation.Implementation;

@Implementation
public class DocsUpdater {
	public void updateTitle(Docs docs, String title) {
		docs.updateTitle(title);
	}

	public void updateType(Docs docs, DocsType docsType) {
		docs.updateDocsType(docsType);
	}

	public void updateModifiedAt(Docs docs, LocalDateTime localDateTime) {
		docs.updateModifiedAt(localDateTime);
	}

}
