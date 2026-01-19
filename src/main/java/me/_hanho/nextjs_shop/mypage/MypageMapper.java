package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.Review;
import me._hanho.nextjs_shop.model.UserAddress;

@Mapper
public interface MypageMapper {

	List<UserCouponDTO> getUserCoupons(Integer userNo);

	List<MyOrderGroupDTO> getMyOrderListGroupList(Integer userNo);
	
	List<OrderItemWithReviewDTO> getMyOrderListProductWithReview(int orderId);
	
	MyOrderDetailDTO getMyOrderDetail(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	List<MyOrderDetailItemDTO> getMyOrderDetailItems(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	void insertReview(@Param("review") Review review, @Param("userNo") Integer userNo);
	
	void unselectOutOfStockItems(Integer userNo);
	
	List<CartProductDTO> getCartList(Integer userNo);
	
	int updateCart(UpdateCartRequest cart);
	
	int updateSelectedCart(UpdateSelectedCartDTO selectedCart);
	
	int deleteCart(@Param("cartId") List<Integer> cartId, @Param("userNo") Integer userNo);
	
	List<WishlistItemDTO> getWishlistItems(Integer userNo);

	List<UserAddress> getUserAddressList(Integer userNo);

	void insertUserAddress(AddUserAddressRequest userAddress);

	void clearDefaultAddress(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);
	
	int updateUserAddress(UpdateUserAddressRequest userAddress);

	int deleteUserAddress(@Param("addressId") int addressId, @Param("userNo") Integer userNo);
	
}
