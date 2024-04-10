package com.project.bumawiki.domain.docs.presentation.dto.response;

import static org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.*;

import java.util.ArrayList;

import com.project.bumawiki.domain.docs.domain.type.DocsType;

public record VersionDocsDiffResponseDto(
	String title,
	DocsType docsType,
	VersionDocsSummaryResponseDto versionDocs,
	ArrayList<Diff> diff
) {
}
