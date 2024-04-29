package com.project.bumawiki.domain.file.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bumawiki.domain.file.presentation.dto.R2FileResponseDto;
import com.project.bumawiki.domain.file.service.CommandFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class R2Controller {
	private final CommandFileService commandFileService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public R2FileResponseDto upload(@RequestPart("file") MultipartFile file) {
		return R2FileResponseDto.from(
			commandFileService.uploadFile(file)
		);
	}
}
