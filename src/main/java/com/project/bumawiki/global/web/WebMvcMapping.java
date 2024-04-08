package com.project.bumawiki.global.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcMapping implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(
				"http://localhost:3000",
				"http://localhost:3001",
				"http://localhost:3002",
				"https://buma.wiki"
			)
			.allowedMethods("*")
			.allowedHeaders("*");
	}
}
