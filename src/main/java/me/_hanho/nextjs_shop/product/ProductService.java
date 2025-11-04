package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Wish;

@Service
public class ProductService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductMapper productMapper;
	
	public List<ProductListDTO> getProductList(String sort, Integer menuSubId, Timestamp lastCreatedAt, Integer lastProductId,
			Integer lastPopularity) {
		return productMapper.getProductList(sort, menuSubId, lastCreatedAt, lastProductId, lastPopularity);
	}
	
	public void addToWishList(Wish wish) {
		productMapper.addToWishList(wish);
	}

	public void deleteWish(String wishId) {
		productMapper.deleteWish(wishId);
	}

	public void putCart(Cart cart) {
		productMapper.putCart(cart);
	}

}
