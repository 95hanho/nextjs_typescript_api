package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.ProductDetail;
import me._hanho.nextjs_shop.model.Review;
import me._hanho.nextjs_shop.model.UserAddress;

@Mapper
public interface MypageMapper {

	List<UserCouponDTO> getUserCoupons(String userId);

	List<MyOrderGroupDTO> getMyOrderListGroupList(String userId);
	
	List<OrderItemWithReviewDTO> getMyOrderListProductWithReview(int orderId);
	
	MyOrderDetailDTO getMyOrderDetail(String orderId);
	
	List<MyOrderDetailItemDTO> getMyOrderDetailItems(String orderId);
	
	void insertReview(Review review);
	
	void unselectOutOfStockItems(String userId);
	
	List<CartProductDTO> getCartList(String userId);
	
	int updateCart(Cart cart);
	
	int updateSelectedCart(UpdateSelectedCartDTO selectedCart);
	
	int deleteCart(int cartId);
	
	List<ProductDetail> getCartOptionProductDetailList(int productId);
	
	List<WishlistItemDTO> getWishlistItems(String userId);

	List<UserAddress> getUserAddressList(String userId);

	void insertUserAddress(UserAddress userAddress);

	int updateUserAddress(UserAddress userAddress);

	int deleteUserAddress(int addressId);


	

	

	

	


	

	
	
	
}
