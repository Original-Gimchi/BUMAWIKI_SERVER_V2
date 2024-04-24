package com.project.bumawiki.global.s3.implement;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.bumawiki.global.config.s3.S3Bucket;
import com.project.bumawiki.global.s3.exception.S3SaveException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageCreator {
	private final S3Bucket s3Bucket;
	private final AmazonS3 amazonS3;

	public String create(MultipartFile multipartFile) {
		String fileName = createFileName(multipartFile);

		try {
			PutObjectRequest request = new PutObjectRequest(
				s3Bucket.getS3Bucket(),
				fileName,
				multipartFile.getInputStream(),
				getMetadata(multipartFile)
			);

			amazonS3.putObject(request);
		} catch (IOException e) {
			throw new S3SaveException();
		}

		return s3Bucket.getReadUrl() + fileName;
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
