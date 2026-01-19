package me._hanho.nextjs_shop.seller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.auth.UserNotFoundException;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.ProductOption;

@Service
@RequiredArgsConstructor
public class SellerService {
	
	private final SellerMapper sellerMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	public SellerLoginDTO isSeller(String sellerId) {
		return sellerMapper.isSeller(sellerId);
	}
	public boolean passwordCheck(String password, String checkPassword) {
		return passwordEncoder.matches(password, checkPassword);
	}
	public void insertToken(SellerToken token) {
		sellerMapper.insertToken(token);
	}
	public Integer getSellerNoByToken(TokenDTO token) {
		return sellerMapper.getSellerNoByToken(token);
	}
	public SellerInfoResponse getSeller(int sellerNo) {
		return sellerMapper.getSeller(sellerNo);
	}
	public void setSeller(SellerRegisterRequest seller) {
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));
		sellerMapper.setSeller(seller);
	}
	public List<SellerProductDTO> getSellerProductList(Integer sellerNo) {
		List<SellerProductDTO> sellerProductList = sellerMapper.getSellerProductList(sellerNo);
		
		if(sellerProductList.size() == 0) {
			return sellerProductList;
        }
		// 2) ID 수집
        List<Integer> ids = sellerProductList.stream()
                .map(SellerProductDTO::getProductId)
                .toList();
        
        // 3) 상세 일괄 조회 (IN (...))
        System.out.println("selectDetailsByProductIds : " + ids);
       
        List<ProductOption> details = sellerMapper.selectDetailsByProductIds(ids);
        
        // 4) productId -> details 그룹핑
        Map<Integer, List<ProductOption>> byProductId = details.stream()
                .collect(Collectors.groupingBy(ProductOption::getProductId));

        // 5) 각 상품 DTO에 붙이기
        for (SellerProductDTO p : sellerProductList) {
            List<ProductOption> list = byProductId.getOrDefault(p.getProductId(), Collections.emptyList());
            p.setOptionList(list);
        }
        
		return sellerProductList;
	}
	public void addProduct(AddProductRequest product) {
		sellerMapper.addProduct(product);
	}
	public void updateProduct(UpdateProductRequest product) {
		int updated = sellerMapper.updateProduct(product);
	 if (updated == 0) {
	        throw new UserNotFoundException("product not found: " + product.getProductId());
	    }
	}
	public void addProductOption(ProductOption productOption) {
		sellerMapper.addProductOption(productOption);
	}
	public void updateProductOption(ProductOption productOption) {
		int updated = sellerMapper.updateProductOption(productOption);
	    if (updated == 0) {
	        throw new UserNotFoundException("productOption not found: " + productOption.getProductOptionId());
	    }
	}
	public List<Coupon> getSellerCouponList(Integer sellerNo) {
		return sellerMapper.getSellerCouponList(sellerNo);
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
	public List<ProductViewCountDTO> getProductViewCountList(Integer sellerNo) {
		return sellerMapper.getProductViewCountList(sellerNo);
	}
	public List<ProductWishCountDTO> getProductWishCountList(Integer sellerNo) {
		return sellerMapper.getProductWishCountList(sellerNo);
	}
	public List<userInBookmarkDTO> getBrandBookmarkList(Integer sellerNo) {
		return sellerMapper.getBrandBookmarkList(sellerNo);
	}
	public List<UserInCartCountDTO> getUserInCartCountList(Integer sellerNo) {
		return sellerMapper.getUserInCartCountList(sellerNo);
	}



	//









	

}
