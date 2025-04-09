package me._hanho.nextjs_shop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me._hanho.nextjs_shop.mapper.MainMapper;
import me._hanho.nextjs_shop.model.Product;

@Repository
public class MainRepository {

	
	@Autowired
	private MainMapper mainMapper;

	public List<Product> getMainImages() {
		return mainMapper.getMainImages();
	}
}
