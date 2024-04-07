package com.project.bumawiki.domain.image.presentation;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "image")
public class FileStorageProperties {
	private final String path;
}
