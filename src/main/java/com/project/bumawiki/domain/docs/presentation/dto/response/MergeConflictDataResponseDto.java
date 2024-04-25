package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.util.LinkedList;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

public record MergeConflictDataResponseDto(
	String firstDocsContent,
	String secondDocsContent,
	String originalDocsContent,
	LinkedList<DiffMatchPatch.Diff> diff1,
	LinkedList<DiffMatchPatch.Diff> diff2,
	Integer lastVersion
) {
}
