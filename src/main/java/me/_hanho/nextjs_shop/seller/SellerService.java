package me._hanho.nextjs_shop.seller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.dto.ReToken;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.file.FileService;
import me._hanho.nextjs_shop.file.dto.FileUploadRequest;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.seller.dto.AddCouponRequest;
import me._hanho.nextjs_shop.seller.dto.AddFileMeta;
import me._hanho.nextjs_shop.seller.dto.AddProductOptionRequest;
import me._hanho.nextjs_shop.seller.dto.AddProductRequest;
import me._hanho.nextjs_shop.seller.dto.ProductImageResponse;
import me._hanho.nextjs_shop.seller.dto.ProductViewCountResponse;
import me._hanho.nextjs_shop.seller.dto.ProductWishCountResponse;
import me._hanho.nextjs_shop.seller.dto.SellerCouponResponse;
import me._hanho.nextjs_shop.seller.dto.SellerInfoResponse;
import me._hanho.nextjs_shop.seller.dto.SellerLogin;
import me._hanho.nextjs_shop.seller.dto.SellerProductDetailResponse;
import me._hanho.nextjs_shop.seller.dto.SellerProductResponse;
import me._hanho.nextjs_shop.seller.dto.SellerQnaResponse;
import me._hanho.nextjs_shop.seller.dto.SellerRegisterRequest;
import me._hanho.nextjs_shop.seller.dto.SellerReviewResponse;
import me._hanho.nextjs_shop.seller.dto.SellerToken;
import me._hanho.nextjs_shop.seller.dto.SetProductImageRequest;
import me._hanho.nextjs_shop.seller.dto.UpdateCouponRequest;
import me._hanho.nextjs_shop.seller.dto.UpdateProductOptionRequest;
import me._hanho.nextjs_shop.seller.dto.UpdateProductRequest;
import me._hanho.nextjs_shop.seller.dto.UserInBookmarkResponse;
import me._hanho.nextjs_shop.seller.dto.UserInCartCountResponse;
import me._hanho.nextjs_shop.util.CouponCodeGenerator;

@Service
@RequiredArgsConstructor
public class SellerService {
	
	private final SellerMapper sellerMapper;
	private final FileService fileService;
	
	private final PasswordEncoder passwordEncoder;
	
	public SellerLogin isSeller(String sellerId) {
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
	public Integer addProduct(AddProductRequest product, Integer sellerNo) {
		sellerMapper.addProduct(product, sellerNo);
		return product.getProductId();
	}
	public void updateProduct(UpdateProductRequest product, Integer sellerNo) {
	    int updated = sellerMapper.updateProduct(product, sellerNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.NO_PERMISSION_OR_PRODUCT_NOT_FOUND);
	    }
	}
	@Transactional
	public void setProductImages(SetProductImageRequest productImageRequest, List<MultipartFile> files, Integer sellerNo) {
		// 1. 상품 소유자 검증
		// 2. 요청 유효성 검증
		// 3. 삭제 처리(nextjs_shop_file is_deleted = true)
		if(productImageRequest.getDeleteImageIds().size() > 0) {
			sellerMapper.deleteProductImages(productImageRequest.getDeleteImageIds(), sellerNo);
		}
		// 4. 수정 처리
		if(productImageRequest.getUpdateFiles().size() > 0) {
			sellerMapper.updateProductImages(productImageRequest.getUpdateFiles(), sellerNo);
		}
		// 5. 추가 처리
		Integer productId = productImageRequest.getProductId();
		List<AddFileMeta> addFiles = productImageRequest.getAddFiles();
		List<String> storeNames = new ArrayList<>(); // 업로드된 파일명 저장 (추가 처리 중 오류 발생 시, 업로드된 파일 삭제 위해)
		try {
			if (addFiles != null && !addFiles.isEmpty()) {
				if (files == null || addFiles.size() != files.size()) {
					throw new BusinessException(ErrorCode.BAD_REQUEST);
				}
				for (int i = 0; i < addFiles.size(); i++) {
					AddFileMeta meta = addFiles.get(i);
					MultipartFile file = files.get(i);
					FileUploadRequest fileUploadRequest = fileService.fileUploadImage(file);
					storeNames.add(fileUploadRequest.getStoreName());
					meta.setFileId(fileUploadRequest.getFileId());
					sellerMapper.insertProductImage(meta, productId, sellerNo);
				}
			}

		} catch (Exception e) {
			// 5-1. 추가 처리 중 오류 발생 시, 업로드된 파일들 삭제
			for (String storeName : storeNames) {
				try {
					fileService.deleteFile(storeName);
				} catch (Exception deleteEx) {
					// 로그만 남기기
				}
			}
			throw e;
		}
		// 6. 썸네일/정렬 최종 보정
	}
	public SellerProductDetailResponse getProductDetail(Integer productId, Integer sellerNo) {
		SellerProductDetailResponse productDetail = sellerMapper.getProductDetail(productId, sellerNo);
		if (productDetail == null) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_OR_PRODUCT_NOT_FOUND);
		}
		// 이미지 조회
		List<ProductImageResponse> productImages = sellerMapper.getProductImages(productId);
		productDetail.setProductImages(productImages);
		return productDetail;
	}
	public boolean isProductNameDuplicate(String productName) {
		Integer count = sellerMapper.isProductNameDuplicate(productName);
		return count != null && count > 0;
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
	public boolean isCouponDescriptionDuplicate(String description, Integer sellerNo) {
		Integer couponCount = sellerMapper.getSellerCouponCountByDescription(description, sellerNo);
		return couponCount != null && couponCount > 0;
	}
	
	public List<Integer> getProductIdsForCouponAllow(Integer couponId, Integer sellerNo) {
		return sellerMapper.getProductIdsForCouponAllow(couponId, sellerNo);
	}
	public void insertSellerCouponAllow(Integer couponId, List<Integer> addProductIds, Integer sellerNo) {
	    sellerMapper.insertSellerCouponAllowList(couponId, addProductIds, sellerNo);
	}
	public void deleteSellerCouponAllow(Integer couponId, List<Integer> removeProductIds,  Integer sellerNo) {
	    sellerMapper.deleteSellerCouponAllowList(couponId, removeProductIds, sellerNo);
	}
	public void activateCoupons(List<Integer> suspendedCouponIds,  Integer sellerNo) {
	    sellerMapper.activateCoupons(suspendedCouponIds, sellerNo);
	}
	public void suspendCoupons(List<Integer> activeCouponIds,  Integer sellerNo) {
	    sellerMapper.suspendCoupons(activeCouponIds, sellerNo);
	}
	public List<SellerReviewResponse> getSellerReviewList(Integer sellerNo) {
		return sellerMapper.getSellerReviewList(sellerNo);
	} 
	public List<SellerQnaResponse> getSellerQnaList(Integer sellerNo) {
		return sellerMapper.getSellerQnaList(sellerNo);
	}
	public void updateQnaAnswer(Integer productQnaId, String answer, Integer sellerNo) {
		int result = sellerMapper.updateQnaAnswer(productQnaId, answer, sellerNo);
		// answerRead가 true인 경우 result는 0
		if(result == 0) {
			throw new BusinessException(ErrorCode.QNA_ANSWER_ALREADY_READ, "QnA answer already read: " + productQnaId);
		}
	}
	/* --- */
	public List<ProductViewCountResponse> getViewedUserList(Integer sellerNo) {
		return sellerMapper.getViewedUserList(sellerNo);
	}
	public List<ProductWishCountResponse> getProductWishCountList(Integer sellerNo) {
		return sellerMapper.getProductWishCountList(sellerNo);
	}
	public List<UserInBookmarkResponse> getBrandBookmarkList(Integer sellerNo) {
		return sellerMapper.getBrandBookmarkList(sellerNo);
	}
	public List<UserInCartCountResponse> getUserInCartCountList(Integer sellerNo) {
		return sellerMapper.getUserInCartCountList(sellerNo);
	}





	//









	

}
