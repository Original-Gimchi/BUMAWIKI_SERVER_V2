package com.project.bumawiki.domain.docs.presentation.dto.response;

import static org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.*;

import java.util.ArrayList;

import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.VersionDocsSummaryDto;

public record VersionDocsDiffResponseDto(
	String title,
	DocsType docsType,
	VersionDocsSummaryDto versionDocs,
	ArrayList<Diff> diff
) {
}
