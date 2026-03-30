package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.OrderItemCoupon;
import me._hanho.nextjs_shop.mypage.dto.AddReviewRequest;
import me._hanho.nextjs_shop.mypage.dto.AddUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.AvailableCartCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.AvailableSellerCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.CartProductResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailItem;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderGroupResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderItemResponse;
import me._hanho.nextjs_shop.mypage.dto.UpdateCartRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.UserAddressResponse;
import me._hanho.nextjs_shop.mypage.dto.UserCouponResponse;
import me._hanho.nextjs_shop.mypage.dto.WishlistItemResponse;

@Mapper
public interface MypageMapper {

	List<UserCouponResponse> getUserCoupons(Integer userNo);

	List<MyOrderGroupResponse> getMyOrderListGroupList(@Param("userNo") Integer userNo, @Param("keyword") String keyword);

	List<MyOrderItemResponse> getMyOrderItemsByOrderIds(@Param("orderIds") List<Integer> orderIds);
	
	MyOrderDetailResponse getMyOrderDetail(@Param("orderId") String orderId, @Param("userNo") Integer userNo);
	
	List<MyOrderDetailItem> getMyOrderDetailItems(@Param("orderId") String orderId, @Param("userNo") Integer userNo);

	List<OrderItemCoupon> getOrderItemCouponsByOrderItemIds(@Param("orderItemIds") List<Integer> orderItemIds);
	
	void insertReview(@Param("review") AddReviewRequest review, @Param("userNo") Integer userNo);
	
	void releaseHoldIfExists(Integer userNo);

	int unselectOutOfStockItems(Integer userNo);
	
	List<CartProductResponse> getCartList(Integer userNo);

	List<AvailableCartCouponAtCartResponse> getAvailableCartCouponsAtCart(@Param("productIds") List<Integer> productIds, @Param("userNo") Integer userNo);

	List<AvailableSellerCouponAtCartResponse> getAvailableSellerCouponsAtCart(@Param("productIds") List<Integer> productIds, @Param("userNo") Integer userNo);
	
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
