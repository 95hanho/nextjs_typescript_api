package me._hanho.nextjs_shop.buy;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.ProductDetail;

@RestController
@RequestMapping("/bapi/buy")
public class BuyController {
	
	private static final Logger logger = LoggerFactory.getLogger(BuyController.class);
	
	@Autowired
	private BuyService buyService;
	
	// 상품 구입
	@PostMapping
	public ResponseEntity<Map<String, Object>> buyProduct(@ModelAttribute ProductDetail productDetail) {
		logger.info("buyProduct " + productDetail);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 재고 - 1, 판매량 + 1
		buyService.updateProductDetailByBuy(productDetail);

		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
}
