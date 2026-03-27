package me._hanho.nextjs_shop.main;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.main.dto.MenuResponse;
import me._hanho.nextjs_shop.main.dto.MenuSubDTO;
import me._hanho.nextjs_shop.main.dto.ProductMainSlideResponse;

@Mapper
public interface MainMapper {
	
	List<MenuResponse> getMenuList();
	
	List<MenuSubDTO> getMenuSubList(int menuTopId);

	List<ProductMainSlideResponse> getMainSlideProducts();

}
