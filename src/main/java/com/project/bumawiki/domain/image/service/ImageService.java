package com.project.bumawiki.domain.image.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.bumawiki.domain.image.exception.NoImageException;

@Service
public class ImageService {
	private static final String uploadPath = "/home/insert/Desktop/image/";

	private static String getRandomStr() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		SecureRandom random = new SecureRandom();
		String generatedString = random.ints(leftLimit, rightLimit + 1)
			.limit(targetStringLength)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
		System.out.println("random : " + generatedString);
		return generatedString;
	}

	public String saveFile(MultipartFile file, String userName) throws IOException {

		String randomStr = getRandomStr();
		String fileName = randomStr + StringUtils.cleanPath(file.getOriginalFilename());

		Path uploadPath = Paths.get(ImageService.uploadPath + userName);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = file.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public ArrayList<String> getFileUrl(MultipartFile[] files, String docsName) throws IOException {
		ArrayList<String> imageUrl = new ArrayList<>();
		for (MultipartFile file : files) {
			String fileName = saveFile(file, docsName);
			imageUrl.add("<<https://bumawiki.kro.kr/api/image/display/" + docsName + "/" + fileName + ">>");
		}
		return imageUrl;
	}

	public Resource loadFileAsResource(String docsName, String fileName) {
		Path uploadPath = Paths.get(ImageService.uploadPath, docsName);
		try {
			Path filePath = uploadPath.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw NoImageException.EXCEPTION;
			}
		} catch (MalformedURLException ex) {
			System.out.println(docsName + "/" + fileName);
			throw NoImageException.EXCEPTION;
		}
	}
}
