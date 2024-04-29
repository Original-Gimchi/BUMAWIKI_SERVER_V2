package com.project.bumawiki.global.config.file.r2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class R2Bucket {
	@Value("${aws.s3.bucket}")
	private String s3Bucket;

	@Value("${aws.s3.read-url}")
	private String readUrl;
}
