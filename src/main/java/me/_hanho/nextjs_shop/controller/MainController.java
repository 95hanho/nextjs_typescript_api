package me._hanho.nextjs_shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.service.MainService;

@RestController
@RequestMapping("/bapi/main")
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private MainService mainService;

	//
	@GetMapping
	public ResponseEntity<Map<String, Object>> getMainInfo() {
		logger.info("getMain");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Product> product_list = mainService.getMainImages();
		
		result.put("msg", "success");
		result.put("productList", product_list);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
