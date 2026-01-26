package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MypageMapper {

	List<UserCouponDTO> getUserCoupons(Integer userNo);

	List<MyOrderGroupDTO> getMyOrderListGroupList(Integer userNo);
	
	List<OrderItemWithReviewDTO> getMyOrderListProductWithReview(int orderId);
	
	MyOrderDetailDTO getMyOrderDetail(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	List<MyOrderDetailItemDTO> getMyOrderDetailItems(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	void insertReview(@Param("review") AddReviewRequest review, @Param("userNo") Integer userNo);
	
	void unselectOutOfStockItems(Integer userNo);
	
	List<CartProductDTO> getCartList(Integer userNo);
	
	int updateCart(@Param("c") UpdateCartRequest cart, @Param("userNo") Integer userNo);
	
	int updateSelectedCart(@Param("cartIdList") List<Integer> cartIdList, 
							@Param("selected") Boolean selected, 
							@Param("userNo") Integer userNo);
	
	int deleteCart(@Param("cartIdList") List<Integer> cartIdList, @Param("userNo") Integer userNo);
	
	List<WishlistItemDTO> getWishlistItems(Integer userNo);

	List<UserAddressResponse> getUserAddressList(Integer userNo);

	void insertUserAddress(@Param("ua") AddUserAddressRequest userAddress, @Param("userNo") Integer userNo);

	void clearDefaultAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	int updateUserAddress(@Param("ua") UpdateUserAddressRequest userAddress, @Param("userNo") Integer userNo);

	int isDefaultAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	int deleteUserAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	void updateDefaultLatest(@Param("userNo") Integer userNo);

	

	

	
	
}
