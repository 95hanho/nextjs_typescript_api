package me._hanho.nextjs_shop.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	void fileUpload(MultipartFile file, String id);

	String getOriginalFile(String id);

	String getStoredFile(String id);

}
