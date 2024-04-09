package com.project.bumawiki.domain.image;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.image.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {
	private final ImageService imageService;

	@GetMapping("/display/{docsName}/{fileName}")
	public ResponseEntity<Resource> displayImage(@PathVariable String fileName,
		@PathVariable String docsName,
		HttpServletRequest request) {
		// Load file as Resource
		Resource resource = imageService.loadFileAsResource(docsName, fileName);
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.header("Content-Type", contentType)
			.body(resource);
	}

}
