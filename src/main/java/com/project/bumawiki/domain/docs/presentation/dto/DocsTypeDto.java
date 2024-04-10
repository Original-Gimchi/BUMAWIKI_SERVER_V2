package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DocsTypeDto {
	private Map<Integer, List<DocsNameAndEnrollResponseDto>> data;
	private Set<Integer> keys;

	public static DocsTypeDto from(List<Docs> allDocs) {

		List<DocsNameAndEnrollResponseDto> docsList = allDocs.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();

		// TreeMap을 사용하여 enroll 값을 기준으로 정렬된 Map을 만듭니다.
		Map<Integer, List<DocsNameAndEnrollResponseDto>> enrollMap = new TreeMap<>(Collections.reverseOrder());

		// 기존의 리스트를 순회하면서 enroll 값을 기준으로 Map에 추가합니다.
		for (DocsNameAndEnrollResponseDto doc : docsList) {
			enrollMap.computeIfAbsent(doc.getEnroll(), k -> new ArrayList<>()).add(doc);
		}

		return new DocsTypeDto(
			enrollMap,
			enrollMap.keySet()
		);
	}
}
