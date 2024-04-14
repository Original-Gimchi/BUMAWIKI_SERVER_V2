package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.LinkedList;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import com.project.bumawiki.domain.docs.presentation.dto.response.ContentVersionDocsResponseDto;

public record MergeConflictDataResponse(
	ContentVersionDocsResponseDto firstDocs,
	String updatingContents,
	ContentVersionDocsResponseDto originalDocs,
	LinkedList<DiffMatchPatch.Diff> diff1,
	LinkedList<DiffMatchPatch.Diff> diff2
) {}
