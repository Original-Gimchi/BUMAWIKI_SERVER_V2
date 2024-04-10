package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.LinkedList;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

public record MergeConflictDataResponse(
	String firstDocsContent,
	String secondDocsContent,
	String originalDocsContent,
	LinkedList<DiffMatchPatch.Diff> diff1,
	LinkedList<DiffMatchPatch.Diff> diff2
) {
}
