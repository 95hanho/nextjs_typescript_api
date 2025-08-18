package me._hanho.nextjs_shop.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.MenuTop;
import me._hanho.nextjs_shop.model.Product;

@Service
public class MainService {
	
	@Autowired
	private MainMapper mainMapper;
	
	public List<MenuTop> getMenusWithSubs() {
		return mainMapper.getMenusWithSubs();
	}

	public List<ProductMainSlideDto> getMainSlideProducts() {
		return mainMapper.getMainSlideProducts();
	}


}
