package com.project.bumawiki.domain.file.presentation;

import com.project.bumawiki.domain.file.presentation.dto.FileResponseDto;
import com.project.bumawiki.domain.file.presentation.dto.R2FileResponseDto;
import com.project.bumawiki.domain.file.service.CommandFileService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
	private final CommandFileService commandFileService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public R2FileResponseDto upload(@RequestPart("file") MultipartFile file) {
		return new R2FileResponseDto(
			commandFileService.uploadFile(file)
		);
	}

	@GetMapping("/display/{docsName}/{fileName}")
	public ResponseEntity<Resource> displayImage(
		@PathVariable String fileName,
		@PathVariable String docsName,
		HttpServletRequest request
	) {
		FileResponseDto fileResponseDto = commandFileService.getFile(docsName, fileName, request);
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(fileResponseDto.contentType()))
			.header("Content-Type", fileResponseDto.contentType())
			.body(fileResponseDto.resource());
	}

}
