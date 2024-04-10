package com.project.bumawiki.domain.docs.util;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

public class DocsUtil {
	private static final DiffMatchPatch dmp = new DiffMatchPatch();

	public static String getThumbnail(String contents) {
		String pattern = "(?<=<사진 [^>]*>)(.*?)(?=</사진>)|(?<=<<)([^>]*)(?=>>:\\{)";

		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(contents);

		if (matcher.find()) {
			return matcher.group(0);
		} else {
			return null;
		}

	}

	public static LinkedList<DiffMatchPatch.Diff> getDiff(String base, String target) {
		LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(base, target);
		dmp.diffCleanupSemantic(diff);
		return diff;
	}
}
