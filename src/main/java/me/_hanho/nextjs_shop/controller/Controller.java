package me._hanho.nextjs_shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class Controller {

	@GetMapping
	public String mainUrl() {
		return "api_spring_default";
	}
	
}
