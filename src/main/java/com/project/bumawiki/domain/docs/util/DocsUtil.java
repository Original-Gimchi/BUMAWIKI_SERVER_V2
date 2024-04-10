package com.project.bumawiki.domain.docs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocsUtil {
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
}
