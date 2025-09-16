package me._hanho.nextjs_shop.buy;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.ProductDetail;

@Mapper
public interface BuyMapper {

	void updateProductDetailByBuy(ProductDetail productDetail);

}
