package me._hanho.nextjs_shop.buy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.ProductDetail;

@Service
public class BuyService {
	
	@Autowired
	private BuyMapper buyMapper;

	public void updateProductDetailByBuy(ProductDetail productDetail) {
		buyMapper.updateProductDetailByBuy(productDetail);
	}

}
