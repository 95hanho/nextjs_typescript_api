package me._hanho.nextjs_shop.seller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.ReToken;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.util.CouponCodeGenerator;

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
	public void insertToken(SellerToken token, Integer sellerNo) {
		sellerMapper.insertToken(token, sellerNo);
	}
	public Integer getSellerNoByToken(ReToken token) {
		return sellerMapper.getSellerNoByToken(token);
	}
	public SellerInfoResponse getSeller(int sellerNo) {
		return sellerMapper.getSeller(sellerNo);
	}
	public boolean hasId(String sellerId) {
		return sellerMapper.hasId(sellerId) == 1;
	}
	public void setSeller(SellerRegisterRequest seller) {
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));
		sellerMapper.setSeller(seller);
	}
	public List<SellerProductResponse> getSellerProductList(Integer sellerNo) {
		List<SellerProductResponse> sellerProductList = sellerMapper.getSellerProductList(sellerNo);
		
		if(sellerProductList.size() == 0) {
			return sellerProductList;
        }
		// 2) ID 수집
        List<Integer> ids = sellerProductList.stream()
                .map(SellerProductResponse::getProductId)
                .toList();
        
        // 3) 상세 일괄 조회 (IN (...))
        System.out.println("selectDetailsByProductIds : " + ids);
       
        List<ProductOption> details = sellerMapper.selectDetailsByProductIds(ids);
        
        // 4) productId -> details 그룹핑
        Map<Integer, List<ProductOption>> byProductId = details.stream()
                .collect(Collectors.groupingBy(ProductOption::getProductId));

        // 5) 각 상품 DTO에 붙이기
        for (SellerProductResponse p : sellerProductList) {
            List<ProductOption> list = byProductId.getOrDefault(p.getProductId(), Collections.emptyList());
            p.setOptionList(list);
        }
        
		return sellerProductList;
	}
	public void addProduct(AddProductRequest product, Integer sellerNo) {
		sellerMapper.addProduct(product, sellerNo);
	}
	public void updateProduct(UpdateProductRequest product, Integer sellerNo) {
	    int updated = sellerMapper.updateProduct(product, sellerNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.NO_PERMISSION_OR_PRODUCT_NOT_FOUND);
	    }
	}
	public void addProductOption(AddProductOptionRequest productOption, Integer sellerNo) {
		try {
		    int inserted = sellerMapper.addProductOption(productOption, sellerNo);
		    if (inserted == 0) {
		        // product가 없거나 / 내 상품이 아님
		    	throw new BusinessException(ErrorCode.NO_PERMISSION_OR_PRODUCT_NOT_FOUND);
		    }
		} catch (DuplicateKeyException e) {
			throw new BusinessException(ErrorCode.PRODUCT_OPTION_SIZE_DUPLICATED);
		}
	}
	public void updateProductOption(UpdateProductOptionRequest productOption, Integer sellerNo) {
		try {
			int updated = sellerMapper.updateProductOption(productOption, sellerNo);
		    if (updated == 0) {
		        throw new BusinessException(ErrorCode.PRODUCT_OPTION_NOT_FOUND,
		            "Product option not found: " + productOption.getProductOptionId());
		    }
		} catch (DuplicateKeyException e) {
			throw new BusinessException(ErrorCode.PRODUCT_OPTION_SIZE_DUPLICATED);
		}
	}
	public void deleteProductOption(Integer productOptionId, Integer sellerNo) {
		sellerMapper.deleteProductOption(productOptionId, sellerNo);
	}
	public List<SellerCouponResponse> getSellerCouponList(Integer sellerNo) {
		return sellerMapper.getSellerCouponList(sellerNo);
	}
	@Transactional(readOnly = true)
    public String generateUniqueCouponCode() {
        final int maxTry = 20; // 현실적으로 충분
        for (int i = 0; i < maxTry; i++) {
            String code = CouponCodeGenerator.generateDefault();
            if (sellerMapper.countByCouponCode(code) == 0) {
                return code;
            }
        }
        throw new IllegalStateException("Failed to generate unique coupon code. Please retry.");
    }
	@Transactional
	public void addCoupon(AddCouponRequest coupon, Integer sellerNo) {
		coupon.setCouponCode(generateUniqueCouponCode());
		sellerMapper.addCoupon(coupon, sellerNo);
	}
	public void updateCoupon(UpdateCouponRequest coupon, Integer sellerNo) {
		sellerMapper.updateCoupon(coupon, sellerNo);
	}
	public void deleteCoupon(Integer couponId, Integer sellerNo) {
		sellerMapper.deleteCoupon(couponId, sellerNo);
	}
	public List<SellerProductCouponAllowed> getSellerCouponAllow(String couponId, Integer sellerNo) {
		return sellerMapper.getSellerCouponAllow(couponId, sellerNo);
	}
	public void setSellerCouponAllow(String couponId, List<Integer> productIds, Boolean allow, Integer sellerNo) {
		if (Boolean.TRUE.equals(allow)) {
	        sellerMapper.insertSellerCouponAllowList(couponId, productIds, sellerNo);
	    } else {
	        sellerMapper.deleteSellerCouponAllowList(couponId, productIds, sellerNo);
	    }
	}
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
