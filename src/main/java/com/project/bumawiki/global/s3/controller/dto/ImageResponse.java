package com.project.bumawiki.global.s3.controller.dto;

import lombok.Getter;

@Getter
public final class ImageResponse {
	private final String url;

	public ImageResponse(
		String url
	) {
		this.url = url;
	}

	public static ImageResponse from(String url) {
		return new ImageResponse(url);
	}

	public String url() {
		return url;
	}

}
