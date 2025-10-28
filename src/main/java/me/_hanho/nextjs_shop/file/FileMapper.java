package me._hanho.nextjs_shop.file;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.FileInfo;

@Mapper
public interface FileMapper {

	FileInfo getFile(String fileId);
	

}
