package com.project.bumawiki.global.config.file.local;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public record FileProperties(
	String path
) {
}
