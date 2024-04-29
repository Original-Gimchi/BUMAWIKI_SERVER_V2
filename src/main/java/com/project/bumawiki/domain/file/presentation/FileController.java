package com.project.bumawiki.domain.file.presentation;

import com.project.bumawiki.domain.file.presentation.dto.FileResponseDto;
import com.project.bumawiki.domain.file.service.CommandFileService;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class FileController {
	private final CommandFileService commandFileService;

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
