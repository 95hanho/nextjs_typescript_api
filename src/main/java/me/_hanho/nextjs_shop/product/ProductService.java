package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Like;
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
	
	@Transactional
	public void setLike(Like like) {
		boolean hasLike = productMapper.isLikeExist(like);
		if(!hasLike) {
			productMapper.upProductLike(like.getProductId());
			productMapper.insertLike(like);
		} else {
			productMapper.downProductLike(like.getProductId());
			productMapper.deleteLike(like);
		}
	}
	
	@Transactional
	public void setWish(Wish wish) {
		boolean hasWish = productMapper.isWishExist(wish);
		if(!hasWish) {
			productMapper.upProductWish(wish.getProductId());
			productMapper.insertWish(wish);
		} else {
			productMapper.downProductWish(wish.getProductId());
			productMapper.deleteWish(wish);
		}
	}

	public void putCart(Cart cart) {
		productMapper.putCart(cart);
	}



}
