package me._hanho.nextjs_shop.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileMapper {
	
	String getStoredFile(String id);
	
	String getOriginalFile(String id);

	void fileUpdate(@Param("fileName") String originalFileName, @Param("storeName") String storedFileName, 
			@Param("id") String id);

	void fileInsert(@Param("fileName") String originalFileName, @Param("storeName") String storedFileName, 
			@Param("id") String id);

	

}
