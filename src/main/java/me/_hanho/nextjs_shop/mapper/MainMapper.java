package me._hanho.nextjs_shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.dto.ProductMainSlideDto;
import me._hanho.nextjs_shop.model.MenuTop;
import me._hanho.nextjs_shop.model.Product;

@Mapper
public interface MainMapper {
	
	List<MenuTop> getMenusWithSubs();

	List<ProductMainSlideDto> getMainSlideProducts();

}
