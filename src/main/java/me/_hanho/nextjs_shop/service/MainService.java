package me._hanho.nextjs_shop.service;

import java.util.List;

import me._hanho.nextjs_shop.dto.ProductMainSlideDto;
import me._hanho.nextjs_shop.model.MenuTop;
import me._hanho.nextjs_shop.model.Product;

public interface MainService {

	List<MenuTop> getMenusWithSubs();
	
	List<ProductMainSlideDto> getMainSlideProducts();

}
