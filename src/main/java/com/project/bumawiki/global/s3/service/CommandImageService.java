package com.project.bumawiki.global.s3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bumawiki.global.s3.implement.ImageCreator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandImageService {
	private final ImageCreator imageCreator;

	public String uploadImage(MultipartFile file) {
		return imageCreator.create(file);
	}
}
