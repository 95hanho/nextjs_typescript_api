package me._hanho.nextjs_shop.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
	
	@Autowired
	private MainMapper mainMapper;
	
	public List<MenuDTO> getMenuList() {
		List<MenuDTO> menuList = mainMapper.getMenuList(); 
		menuList.forEach(m -> m.setMenuSubList(mainMapper.getMenuSubList(m.getMenuTopId())));
		return menuList;
	}

	public List<ProductMainSlideDto> getMainSlideProducts() {
		return mainMapper.getMainSlideProducts();
	}


}
