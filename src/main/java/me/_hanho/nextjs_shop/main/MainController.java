package me._hanho.nextjs_shop.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.main.dto.MenuResponse;
import me._hanho.nextjs_shop.main.dto.ProductMainSlideResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/main")
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	private final MainService mainService;
	
	// 메뉴 가져오기
	@GetMapping("/menu")
	public ResponseEntity<Map<String, Object>> getMenuList() {
		logger.info("[getMenuList]");
		Map<String, Object> result = new HashMap<>();

		try {
			List<MenuResponse> menuList = mainService.getMenuList();

			result.put("message", "MENU_FETCH_SUCCESS");
			result.put("menuList", menuList);
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			// 🔥 핵심: 에러 전체 출력
			logger.error("[getMenuList] DB_ERROR", e);

			result.put("message", "DB_ERROR");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 메인 슬라이드 제품 가져오기
	@GetMapping
	public ResponseEntity<Map<String, Object>> getProducts() {
		logger.info("[getProducts]");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductMainSlideResponse> productList = mainService.getMainSlideProducts();
		
		result.put("message", "MAIN_SLIDE_FETCH_SUCCESS");
		result.put("productList", productList);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
