package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.LinkedList;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.presentation.dto.response.ContentVersionDocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsResponseDto;

public record MergeConflictDataResponse(
	ContentVersionDocsResponseDto firstDocs,
	ContentVersionDocsResponseDto secondDocs,
	ContentVersionDocsResponseDto originalDocs,
	LinkedList<DiffMatchPatch.Diff> diff1,
	LinkedList<DiffMatchPatch.Diff> diff2
) {}
