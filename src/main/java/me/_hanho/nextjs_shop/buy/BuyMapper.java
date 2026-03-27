package me._hanho.nextjs_shop.buy;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.buy.dto.AvailabilityRow;
import me._hanho.nextjs_shop.buy.dto.AvailableCartCouponAtBuyResponse;
import me._hanho.nextjs_shop.buy.dto.AvailableSellerCouponAtBuyResponse;
import me._hanho.nextjs_shop.buy.dto.DefaultAddressResponse;
import me._hanho.nextjs_shop.buy.dto.ExistingHold;
import me._hanho.nextjs_shop.buy.dto.HoldBrief;
import me._hanho.nextjs_shop.buy.dto.LatestHoldInfo;
import me._hanho.nextjs_shop.buy.dto.ManageStockHoldCoupon;
import me._hanho.nextjs_shop.buy.dto.OrderItemCouponWithHoldId;
import me._hanho.nextjs_shop.buy.dto.OrderStockResponse;
import me._hanho.nextjs_shop.buy.dto.PayAvailableCoupon;
import me._hanho.nextjs_shop.buy.dto.ShippingAddressRequest;
import me._hanho.nextjs_shop.buy.dto.UpsertHoldRow;
import me._hanho.nextjs_shop.buy.dto.UserCouponRow;
import me._hanho.nextjs_shop.model.OrderGroup;
import me._hanho.nextjs_shop.model.OrderItem;
import me._hanho.nextjs_shop.model.StockHoldCoupon;

@Mapper
public interface BuyMapper {
	
	List<Integer> selectAllActiveHoldsByUser(Integer userNo);

	int releaseHolds(@Param("holdIds") List<Integer> holdIds, @Param("userNo") Integer userNo);

    List<AvailabilityRow> selectAvailability(@Param("detailIds") List<Integer> detailIds);

    List<ExistingHold> selectExistingHolds(@Param("userNo") Integer userNo,
                                           @Param("detailIds") List<Integer> detailIds);

    int upsertHolds(@Param("rows") List<UpsertHoldRow> rows);

    List<HoldBrief> selectLatestHolds(@Param("userNo") Integer userNo,
                                      @Param("detailIds") List<Integer> detailIds);

    int downloadCoupons(@Param("couponIds") List<Integer> couponIds,
                        @Param("userNo") Integer userNo);

    List<UserCouponRow> selectUserCouponsByCouponIds(@Param("userNo") Integer userNo,
                                                     @Param("couponIds") List<Integer> couponIds);

    int deleteHoldCouponsByHoldIds(@Param("holdIds") List<Integer> holdIds);

    int applyCoupons(@Param("holdCoupons") List<StockHoldCoupon> holdCoupons);

    int extendHolds(@Param("holdIds") List<Integer> holdIds, @Param("userNo") Integer userNo, @Param("ttlSeconds") int ttlSeconds);
    
	void addStockHoldCoupons(@Param("holdCouponRequests") List<ManageStockHoldCoupon> holdCouponRequests);

	void deleteStockHoldCoupons(@Param("holdCouponRequests") List<ManageStockHoldCoupon> holdCouponRequests);

	List<OrderStockResponse> getStockHoldProductList(Integer userNo);

	LocalDateTime getMaxCreatedAtStockHold(Integer userNo);

	List<LatestHoldInfo> getLatestHoldsInfo(@Param("userNo") Integer userNo, @Param("latestCreatedAt") LocalDateTime latestCreatedAt);

	List<AvailableCartCouponAtBuyResponse> getAvailableCartCouponsAtBuy(@Param("productIds") List<Integer> productIds, @Param("userNo") Integer userNo);
	
	List<AvailableSellerCouponAtBuyResponse> getAvailableSellerCouponsAtBuy(@Param("productIds") List<Integer> productIds, @Param("userNo") Integer userNo);

	List<StockHoldCoupon> selectHoldCouponsByHoldIds(@Param("holdIds") List<Integer> holdIds);

	List<DefaultAddressResponse> getDefaultAddress(Integer userNo);
	
	// --------------------------
	
	List<OrderStockResponse> getStockHoldProductListByHoldIds(@Param("holdIds") List<Integer> holdIds, @Param("userNo") Integer userNo);

	List<PayAvailableCoupon> getAvailableCouponsByHoldIds(@Param("holdIds") List<Integer> holdIds, @Param("userNo") Integer userNo);

	void insertUserAddress(@Param("address") ShippingAddressRequest address, @Param("setAsDefault") Boolean setAsDefault, @Param("userNo") Integer userNo);

	void updateUserAddressCancelDefault(@Param("userNo") Integer userNo);

	void updateUserAddressDefault(@Param("addressId") Integer addressId, @Param("userNo") Integer userNo);

	int getUserMileage(Integer userNo);

	void insertOrderGroup(OrderGroup orderGroup);

	void insertOrderItems(@Param("orderItems") List<OrderItem> orderItems);

	List<OrderItem> getOrderItems(Integer orderId);

	void insertOrderItemCoupons(@Param("orderItemCoupons") List<OrderItemCouponWithHoldId> orderItemCoupons);

	int updateProductOptionStockAndSalesCount(@Param("holdIds") List<Integer> holdIds);

	void markUserCouponsAsUsed(@Param("userCouponIds") List<Integer> userCouponIds);

	void markCouponsAsUsed(@Param("couponIds") List<Integer> couponIds);

	int updateUserMileage(@Param("usedMileage") int usedMileage, @Param("userNo") int userNo);

	int updateStockHoldStatusToPaid(@Param("holdIds") List<Integer> holdIds, @Param("userNo") Integer userNo);

	void deleteCartItemsByHoldIds(@Param("holdIds") List<Integer> holdIds, @Param("userNo") Integer userNo);

}
