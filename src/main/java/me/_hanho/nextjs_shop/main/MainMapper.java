package me._hanho.nextjs_shop.main;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {
	
	List<MenuResponse> getMenuList();
	
	List<MenuSubDTO> getMenuSubList(int menuTopId);

	List<ProductMainSlideResponse> getMainSlideProducts();

}
