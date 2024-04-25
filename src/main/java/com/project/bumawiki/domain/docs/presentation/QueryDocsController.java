package com.project.bumawiki.domain.docs.presentation;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.ContentsRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.ClubResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsTypeResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.MergeConflictDataResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.TeacherResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsDiffResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.docs.service.QueryDocsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/docs")
public class QueryDocsController {
	private final QueryDocsService queryDocsService;

	@GetMapping("/all/teacher")
	public ResponseEntity<TeacherResponseDto> findAllTeacher() {
		return ResponseEntity.ok(queryDocsService.getAllTeacher());
	}

	@GetMapping("/all/club")
	public ResponseEntity<ClubResponseDto> findAllClub() {
		return ResponseEntity.ok(queryDocsService.getAllClub());
	}

	@GetMapping("/{stringDocsType}")
	public DocsTypeResponseDto findAllByDocsType(
		@PathVariable String stringDocsType) {
		DocsType docsType = DocsType.valueOfLabel(stringDocsType);
		return DocsTypeResponseDto.from(queryDocsService.findByDocsTypeOrderByEnroll(docsType));
	}

	@GetMapping("/find/all/title/{title}")
	public List<DocsNameAndEnrollResponseDto> findAllByTitle(@PathVariable String title) {
		return queryDocsService.findAllByTitle(title)
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}

	@GetMapping("/find/title/{title}")
	public ResponseEntity<DocsResponseDto> findById(@PathVariable String title) {
		return ResponseEntity.ok(queryDocsService.findDocs(title));
	}

	@GetMapping("/find/{title}/version")
	public ResponseEntity<VersionResponseDto> showDocsVersion(@PathVariable String title) {
		return ResponseEntity.ok(queryDocsService.findDocsVersion(title));
	}

	@GetMapping("/find/modified")
	public List<DocsNameAndEnrollResponseDto> showDocsModifiedTimeDesc(
		@PageableDefault(size = 12) Pageable pageable) {
		return queryDocsService.showDocsModifiedAtDesc(pageable)
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}

	@GetMapping("/find/version/{title}/different/{version}")
	public VersionDocsDiffResponseDto showVersionDocsDiff(@PathVariable String title,
		@PathVariable Integer version) {
		return queryDocsService.showVersionDocsDiff(title, version);
	}

	@GetMapping("/find/modified/all")
	public List<DocsNameAndEnrollResponseDto> showDocsModifiedTimeDescAll() {
		return queryDocsService.showDocsModifiedAtDescAll()
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}

	@PostMapping("/merge/{title}")
	@ResponseStatus(HttpStatus.OK)
	public MergeConflictDataResponseDto getMergeConflictData(@PathVariable String title,
		@Valid @RequestBody ContentsRequestDto contentsRequestDto) {
		return queryDocsService.getMergeConflict(title, contentsRequestDto.contents());
	}

	@GetMapping("/popular")
	@ResponseStatus(HttpStatus.OK)
	public List<DocsPopularResponseDto> docsPopular() {
		return queryDocsService.readByThumbsUpsDesc();
	}
}
