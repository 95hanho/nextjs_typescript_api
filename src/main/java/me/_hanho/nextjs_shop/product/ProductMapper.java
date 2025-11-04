package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Wish;

@Mapper
public interface ProductMapper {

	List<ProductListDTO> getProductList(@Param("sort") String sort, @Param("menuSubId") int menuSubId, @Param("lastCreatedAt") Timestamp lastCreatedAt, 
			@Param("lastProductId") Integer lastProductId, @Param("lastPopularity") Integer lastPopularity);
	
	void addToWishList(Wish wish);

	void deleteWish(String wishId);

	void putCart(Cart cart);
}
