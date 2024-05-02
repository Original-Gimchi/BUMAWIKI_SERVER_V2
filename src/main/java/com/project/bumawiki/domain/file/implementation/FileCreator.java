package com.project.bumawiki.domain.file.implementation;

import java.io.IOException;
import java.util.UUID;

import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;

import com.project.bumawiki.global.error.exception.ErrorCode;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.bumawiki.global.config.file.r2.R2Bucket;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class FileCreator {
	private final R2Bucket r2Bucket;
	private final AmazonS3 amazonS3;

	public String create(MultipartFile multipartFile) {
		String fileName = createFileName(multipartFile);

		try {
			PutObjectRequest request = new PutObjectRequest(
				r2Bucket.getS3Bucket(),
				fileName,
				multipartFile.getInputStream(),
				getMetadata(multipartFile)
			);

			amazonS3.putObject(request);
		} catch (IOException e) {
			throw new BumawikiException(ErrorCode.S3_SAVE_EXCEPTION);
		}

		return r2Bucket.getReadUrl() + fileName;
	}

	private String createFileName(MultipartFile multipartFile) {
		return multipartFile.getName() + UUID.randomUUID();
	}

	private ObjectMetadata getMetadata(MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		return metadata;
	}
}
