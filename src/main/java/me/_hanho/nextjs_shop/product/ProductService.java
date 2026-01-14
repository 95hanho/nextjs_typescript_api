package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Like;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.model.ProductQna;
import me._hanho.nextjs_shop.model.Wish;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	private final ProductMapper productMapper;
	
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

	public ProductDetailResponse getProductDetail(int productId) {
		return productMapper.getProductDetail(productId);
	}

	public List<ProductOption> getProductOptionList(int productId) {
		return productMapper.getProductOptionList(productId);
	}

	public List<AvailableProductCouponResponse> getAvailableProductCoupon(int productId, String userId) {
		return productMapper.getAvailableProductCoupon(productId, userId);
	}
	
	public List<ProductReviewResponse> getProductReviewList(String productId, String userId) {
		List<ProductReviewResponse> list = productMapper.getProductReviewList(productId, userId);
		
		if (list == null || list.isEmpty()) return list;
		
		for (ProductReviewResponse review : list) {
	        String writerId = review.getUserId();
	        boolean isOwner = userId != null && writerId != null && writerId.equals(userId);

	        // 1) 작성자가 아니면 userId 마스킹(앞 2글자 + *****)
	        if (!isOwner) {
	        	review.setUserId(maskUserId(writerId));
	        }
	    }

	    return list;
	}

	public List<ProductQna> getProductQnaList(String productId, String userId) {
	    List<ProductQna> list = productMapper.getProductQnaList(productId, userId);

	    if (list == null || list.isEmpty()) return list;

	    for (ProductQna qna : list) {
	        String writerId = qna.getUserId();
	        boolean isOwner = userId != null && writerId != null && writerId.equals(userId);

	        // 1) 작성자가 아니면 userId 마스킹(앞 2글자 + *****)
	        if (!isOwner) {
	            qna.setUserId(maskUserId(writerId));
	        }

	        // 2) 비밀글(secret=1)인데 작성자가 아니면 질문/답변 null 처리
	        //    (판매자/관리자도 원문 열람 가능하게 하려면 여기에서 권한 조건 추가하면 됨)
	        if (!isOwner && Boolean.TRUE.equals(qna.isSecret())) {
	            qna.setQuestion(null);
	            qna.setAnswer(null);
	            // 필요하면 응답시간도 숨김
	            // qna.setResCreatedAt(null);
	        }
	    }

	    return list;
	}

	private String maskUserId(String id) {
	    if (id == null) return null;

	    // 길이가 2 미만이면 있는 만큼만 + ***** 붙임
	    int prefixLen = Math.min(2, id.length());
	    return id.substring(0, prefixLen) + "*****";
	}


}
