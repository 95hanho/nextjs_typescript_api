package me._hanho.nextjs_shop.main;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainService {
	
	private final MainMapper mainMapper;
	
	public List<MenuResponse> getMenuList() {
		List<MenuResponse> menuList = mainMapper.getMenuList(); 
		menuList.forEach(m -> m.setMenuSubList(mainMapper.getMenuSubList(m.getMenuTopId())));
		return menuList;
	}

	public List<ProductMainSlideResponse> getMainSlideProducts() {
		return mainMapper.getMainSlideProducts();
	}


}
