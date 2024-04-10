package com.project.bumawiki.domain.docs.presentation;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.response.ClubResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsTypeResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.TeacherResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsDiffResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.docs.service.DocsInformationService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/docs")
public class DocsInformationController {
	private final DocsInformationService docsInformationService;

	@GetMapping("/all/teacher")
	public ResponseEntity<TeacherResponseDto> findAllTeacher() {
		return ResponseEntity.ok(docsInformationService.getAllTeacher());
	}

	@GetMapping("/all/club")
	public ResponseEntity<ClubResponseDto> findAllClub() {
		return ResponseEntity.ok(docsInformationService.getAllClub());
	}

	@GetMapping("/{stringDocsType}")
	public DocsTypeResponseDto findAllByDocsType(
		@PathVariable String stringDocsType) {
		DocsType docsType = DocsType.valueOfLabel(stringDocsType);
		return DocsTypeResponseDto.from(docsInformationService.findByDocsTypeOrderByEnroll(docsType));
	}

	@GetMapping("/find/all/title/{title}")
	public List<DocsNameAndEnrollResponseDto> findAllByTitle(@PathVariable String title) {
		return docsInformationService.findAllByTitle(title)
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}

	@GetMapping("/find/title/{title}")
	public ResponseEntity<DocsResponseDto> findById(@PathVariable String title) {
		return ResponseEntity.ok(docsInformationService.findDocs(title));
	}

	@GetMapping("/find/{title}/version")
	public ResponseEntity<VersionResponseDto> showDocsVersion(@PathVariable String title) {
		return ResponseEntity.ok(docsInformationService.findDocsVersion(title));
	}

	@GetMapping("/find/modified")
	public List<DocsNameAndEnrollResponseDto> showDocsModifiedTimeDesc(
		@PageableDefault(size = 12) Pageable pageable) {
		return docsInformationService.showDocsModifiedAtDesc(pageable)
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}

	@GetMapping("/find/version/{title}/different/{version}")
	public VersionDocsDiffResponseDto showVersionDocsDiff(@PathVariable String title,
		@PathVariable Integer version) {
		return docsInformationService.showVersionDocsDiff(title, version);
	}

	@GetMapping("/find/modified/all")
	public List<DocsNameAndEnrollResponseDto> showDocsModifiedTimeDescAll() {
		return docsInformationService.showDocsModifiedAtDescAll()
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}
}
