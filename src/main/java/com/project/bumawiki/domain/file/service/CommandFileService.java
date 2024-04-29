package com.project.bumawiki.domain.file.service;

import com.project.bumawiki.domain.file.implementation.FileCreator;
import com.project.bumawiki.domain.file.presentation.dto.FileResponseDto;
import com.project.bumawiki.global.config.file.local.FileProperties;
import com.project.bumawiki.global.error.exception.BumawikiException;

import com.project.bumawiki.global.error.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CommandFileService {
	private final FileCreator fileCreator;
	private final FileProperties fileProperties;

	public String uploadFile(MultipartFile file) {
		return fileCreator.create(file);
	}

	public FileResponseDto getFile(String docsName, String fileName, HttpServletRequest request) {
		Resource resource = loadFileAsResource(docsName, fileName);
		String contentType = getContentType(request, resource);
		return new FileResponseDto(resource, contentType);
	}

	private Resource loadFileAsResource(String docsName, String fileName) {
		Path uploadPath = Paths.get(fileProperties.path(), docsName);
		try {
			Path filePath = uploadPath.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new BumawikiException(ErrorCode.IMAGE_NOT_FOUND_EXCEPTION);
			}
		} catch (MalformedURLException ex) {
			throw new BumawikiException(ErrorCode.MALFORMED_URL);
		}
	}

	private String getContentType(HttpServletRequest request, Resource resource) {
		String contentType = "application/octet-stream";
		try {
			String mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			if (mimeType != null) {
				contentType = mimeType;
			}
		}
		catch (IOException ignored) {
		}
		return contentType;
	}

}
