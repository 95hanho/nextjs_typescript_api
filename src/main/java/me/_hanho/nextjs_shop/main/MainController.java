package me._hanho.nextjs_shop.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.MenuTop;

@RestController
@RequestMapping("/bapi/main")
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private MainService mainService;
	
	// 메뉴 가져오기
	@GetMapping("/menu")
	public ResponseEntity<Map<String, Object>> getMenus() {
		logger.info("getMenus");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<MenuTop> menu_list = mainService.getMenusWithSubs();
		
		result.put("menu_list", menu_list);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 메인 슬라이드 제품 가져오기
	@GetMapping
	public ResponseEntity<Map<String, Object>> getProducts() {
		logger.info("getProducts");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductMainSlideDto> product_list = mainService.getMainSlideProducts();
		
		result.put("msg", "success");
		result.put("productList", product_list);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
