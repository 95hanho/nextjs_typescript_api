package me._hanho.nextjs_shop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me._hanho.nextjs_shop.dto.ProductMainSlideDto;
import me._hanho.nextjs_shop.mapper.MainMapper;
import me._hanho.nextjs_shop.model.MenuTop;
import me._hanho.nextjs_shop.model.Product;

@Repository
public class MainRepository {

	
	@Autowired
	private MainMapper mainMapper;

	public List<MenuTop> getMenusWithSubs() {
		return mainMapper.getMenusWithSubs();
	}
	
	public List<ProductMainSlideDto> getMainSlideProducts() {
		return mainMapper.getMainSlideProducts();
	}

	
}
