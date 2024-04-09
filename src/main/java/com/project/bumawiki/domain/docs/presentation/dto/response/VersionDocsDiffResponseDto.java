package com.project.bumawiki.domain.docs.presentation.dto.response;

import static org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.*;

import java.util.ArrayList;

import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.VersionDocsSummaryDto;

import lombok.Getter;

@Getter
public class VersionDocsDiffResponseDto {

	private final String title;
	private final DocsType docsType;
	private final VersionDocsSummaryDto versionDocs;
	private final ArrayList<Diff> diff;

	public VersionDocsDiffResponseDto(String title, DocsType docsType, VersionDocsSummaryDto versionDocs,
		ArrayList<Diff> diff) {
		this.title = title;
		this.docsType = docsType;
		this.versionDocs = versionDocs;
		this.diff = diff;
	}
}
