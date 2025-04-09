package me._hanho.nextjs_shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me._hanho.nextjs_shop.mapper.FileMapper;

@Repository
public class FileRepository {
	
	@Autowired
	private FileMapper fileMapper;

	public String getStoredFile(String id) {
		return fileMapper.getStoredFile(id);
	}
	
	public String getOriginalFile(String id) {
		return fileMapper.getOriginalFile(id);
	}

	public void fileUpdate(String originalFileName, String storedFileName, String id) {
		fileMapper.fileUpdate(originalFileName, storedFileName, id);
	}

	public void fileInsert(String originalFileName, String storedFileName, String id) {
		fileMapper.fileInsert(originalFileName, storedFileName, id);
	}



}
