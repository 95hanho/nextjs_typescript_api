package me._hanho.nextjs_shop.main;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.main.dto.MenuResponse;
import me._hanho.nextjs_shop.main.dto.MenuSubDTO;
import me._hanho.nextjs_shop.main.dto.ProductMainSlideResponse;

@Service
@RequiredArgsConstructor
public class MainService {
	
	private final MainMapper mainMapper;
	
	public List<MenuResponse> getMenuList() {
		List<MenuResponse> menuList = mainMapper.getMenuList();

		if (menuList.isEmpty()) {
			return menuList;
		}

		List<Integer> menuTopIds = menuList.stream()
			.map(MenuResponse::getMenuTopId)
			.toList();

		List<MenuSubDTO> menuSubList = mainMapper.getMenuSubList(menuTopIds);

		menuList.forEach(m -> m.setMenuSubList(
			menuSubList.stream()
				.filter(sub -> Objects.equals(sub.getMenuTopId(), m.getMenuTopId()))
				.toList()
		));

		return menuList;
	}

	public List<ProductMainSlideResponse> getMainSlideProducts() {
		return mainMapper.getMainSlideProducts();
	}


}
