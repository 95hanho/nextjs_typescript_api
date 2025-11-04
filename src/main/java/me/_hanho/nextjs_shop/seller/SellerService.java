package me._hanho.nextjs_shop.seller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me._hanho.nextjs_shop.auth.UserNotFoundException;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.model.ProductDetail;

@Service
public class SellerService {
	
	@Autowired
	private SellerMapper sellerMapper;
	
	public List<SellerProductDTO> getSellerProductList(String sellerId) {
		List<SellerProductDTO> sellerProductList = sellerMapper.getSellerProductList(sellerId);
		
		// 2) ID 수집
        List<Integer> ids = sellerProductList.stream()
                .map(SellerProductDTO::getProductId)
                .toList();
        
        // 3) 상세 일괄 조회 (IN (...))
        List<ProductDetail> details = sellerMapper.selectDetailsByProductIds(ids);
        
        // 4) productId -> details 그룹핑
        Map<Integer, List<ProductDetail>> byProductId = details.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProductId));

        // 5) 각 상품 DTO에 붙이기
        for (SellerProductDTO p : sellerProductList) {
            List<ProductDetail> list = byProductId.getOrDefault(p.getProductId(), Collections.emptyList());
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
	        throw new UserNotFoundException("product not found: " + product.getProductId());
	    }
	}
	public void addProductDetail(ProductDetail productDetail) {
		sellerMapper.addProductDetail(productDetail);
	}
	public void updateProductDetail(ProductDetail productDetail) {
		int updated = sellerMapper.updateProductDetail(productDetail);
	    if (updated == 0) {
	        throw new UserNotFoundException("productDetail not found: " + productDetail.getProductDetailId());
	    }
	}
	public List<Coupon> getSellerCouponList(String sellerId) {
		return sellerMapper.getSellerCouponList(sellerId);
	}
	public void addCoupon(Coupon coupon) {
		sellerMapper.addCoupon(coupon);
	}
	public void updateCouponStatus(Coupon coupon) {
		sellerMapper.updateCouponStatus(coupon);
	}
	public List<SellerCouponAllowedProductDTO> getSellerCouponAllow(String couponId) {
		return sellerMapper.getSellerCouponAllow(couponId);
	}
	@Transactional
	public void setSellerCouponAllow(String couponId, List<Integer> productIds) {
		sellerMapper.deleteAllsellerCouponAllow(couponId);
		sellerMapper.insertSellerCouponAllowList(couponId, productIds);
	}
	public void issueCouponsToUsers(String couponId, List<String> userIds) {
		sellerMapper.issueCouponsToUsers(couponId, userIds);
	}
	// 
	public List<ProductViewCountDTO> getProductViewCountList(String sellerId) {
		return sellerMapper.getProductViewCountList(sellerId);
	}
	public List<ProductWishCountDTO> getProductWishCountList(String sellerId) {
		return sellerMapper.getProductWishCountList(sellerId);
	}
	public List<userInBookmarkDTO> getBrandBookmarkList(String sellerId) {
		return sellerMapper.getBrandBookmarkList(sellerId);
	}
	public List<UserInCartCountDTO> getUserInCartCountList(String sellerId) {
		return sellerMapper.getUserInCartCountList(sellerId);
	}
	//









	

}
