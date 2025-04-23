package me._hanho.nextjs_shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.MenuTop;
import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.repository.MainRepository;

@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	private MainRepository mainDAO;
	
	@Override
	public List<MenuTop> getMenusWithSubs() {
		return mainDAO.getMenusWithSubs();
	}

	@Override
	public List<Product> getMainImages() {
		return mainDAO.getMainImages();
	}


}
