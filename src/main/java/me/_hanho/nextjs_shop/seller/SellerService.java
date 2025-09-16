package me._hanho.nextjs_shop.seller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me._hanho.nextjs_shop.auth.UserNotFoundException;
import me._hanho.nextjs_shop.model.BrandBookmark;
import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.model.ProductDetail;

@Service
public class SellerService {
	
	@Autowired
	private SellerMapper sellerMapper;
	
	public List<SellerProductDTO> getSellerProductList(String seller_id) {
		List<SellerProductDTO> sellerProductList = sellerMapper.getSellerProductList(seller_id);
		
		// 2) ID 수집
        List<Integer> ids = sellerProductList.stream()
                .map(SellerProductDTO::getProduct_id)
                .toList();
        
        // 3) 상세 일괄 조회 (IN (...))
        List<ProductDetail> details = sellerMapper.selectDetailsByProductIds(ids);
        
        // 4) productId -> details 그룹핑
        Map<Integer, List<ProductDetail>> byProductId = details.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProduct_id));

        // 5) 각 상품 DTO에 붙이기
        for (SellerProductDTO p : sellerProductList) {
            List<ProductDetail> list = byProductId.getOrDefault(p.getProduct_id(), Collections.emptyList());
            p.setDetailList(list);
        }
        
		return sellerProductList;
	}
	public void addProduct(Product product) {
		sellerMapper.addProduct(product);
	}
	public void updateProduct(Product product) {
		int updated = sellerMapper.updateProduct(product);
	 if (updated == 0) {
	        throw new UserNotFoundException("product not found: " + product.getProduct_id());
	    }
	}
	public void addProductDetail(ProductDetail productDetail) {
		sellerMapper.addProductDetail(productDetail);
	}
	public void updateProductDetail(ProductDetail productDetail) {
		int updated = sellerMapper.updateProductDetail(productDetail);
	    if (updated == 0) {
	        throw new UserNotFoundException("productDetail not found: " + productDetail.getProduct_detail_id());
	    }
	}
	public List<Coupon> getSellerCouponList(String seller_id) {
		return sellerMapper.getSellerCouponList(seller_id);
	}
	public void addCoupon(Coupon coupon) {
		sellerMapper.addCoupon(coupon);
	}
	public void updateCouponStatus(Coupon coupon) {
		sellerMapper.updateCouponStatus(coupon);
	}
	public List<SellerCouponAllowedProductDTO> getSellerCouponAllow(String coupon_id) {
		return sellerMapper.getSellerCouponAllow(coupon_id);
	}
	@Transactional
	public void setSellerCouponAllow(String coupon_id, List<Integer> productIds) {
		sellerMapper.deleteAllsellerCouponAllow(coupon_id);
		sellerMapper.insertSellerCouponAllowList(coupon_id, productIds);
	}
	public void issueCouponsToUsers(String coupon_id, List<String> userIds) {
		sellerMapper.issueCouponsToUsers(coupon_id, userIds);
	}
	public List<ProductViewCountDTO> getProductViewCountList(String seller_id) {
		return sellerMapper.getProductViewCountList(seller_id);
	}
	public List<ProductWishCountDTO> getProductWishCountList(String seller_id) {
		return sellerMapper.getProductWishCountList(seller_id);
	}
	public List<userInBookmarkDTO> getBrandBookmarkList(String seller_id) {
		return sellerMapper.getBrandBookmarkList(seller_id);
	}
	public List<UserInCartCountDTO> getUserInCartCountList(String seller_id) {
		return sellerMapper.getUserInCartCountList(seller_id);
	}









	

}
