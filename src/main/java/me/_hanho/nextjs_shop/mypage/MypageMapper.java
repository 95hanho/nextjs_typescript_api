package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MypageMapper {

	List<UserCouponResponse> getUserCoupons(Integer userNo);

	List<MyOrderGroupResponse> getMyOrderListGroupList(Integer userNo);
	
	List<OrderItemWithReview> getMyOrderListProductWithReview(int orderId);
	
	MyOrderDetailResponse getMyOrderDetail(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	List<MyOrderDetailItem> getMyOrderDetailItems(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	void insertReview(@Param("review") AddReviewRequest review, @Param("userNo") Integer userNo);
	
	void unselectOutOfStockItems(Integer userNo);
	
	List<CartProductResponse> getCartList(Integer userNo);
	
	int updateCart(@Param("c") UpdateCartRequest cart, @Param("userNo") Integer userNo);
	
	int updateSelectedCart(@Param("cartIdList") List<Integer> cartIdList, 
							@Param("selected") Boolean selected, 
							@Param("userNo") Integer userNo);
	
	int deleteCart(@Param("cartIdList") List<Integer> cartIdList, @Param("userNo") Integer userNo);
	
	List<WishlistItemResponse> getWishlistItems(Integer userNo);

	List<UserAddressResponse> getUserAddressList(Integer userNo);

	void insertUserAddress(@Param("ua") AddUserAddressRequest userAddress, @Param("userNo") Integer userNo);

	void clearDefaultAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	int updateUserAddress(@Param("ua") UpdateUserAddressRequest userAddress, @Param("userNo") Integer userNo);

	int isDefaultAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	int deleteUserAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	void updateDefaultLatest(@Param("userNo") Integer userNo);

	

	

	
	
}
