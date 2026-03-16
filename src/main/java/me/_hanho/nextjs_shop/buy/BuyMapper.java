package me._hanho.nextjs_shop.buy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    
	List<OrderStockResponse> getStockHoldProductList(Integer userNo);

	List<AvailableCartCouponAtBuyResponse> getAvailableCartCouponsAtBuy(@Param("productIds") List<Integer> productIds, @Param("userNo") Integer userNo);
	
	List<AvailableSellerCouponAtBuyResponse> getAvailableSellerCouponsAtBuy(@Param("productIds") List<Integer> productIds, @Param("userNo") Integer userNo);

	List<StockHoldCoupon> selectHoldCouponsByHoldIds(@Param("holdIds") List<Integer> holdIds);

	List<DefaultAddressResponse> getDefaultAddress(Integer userNo);
	
	List<ProductWithCouponResponse> getProductWithCoupons(@Param("products") List<BuyProduct> products, @Param("userNo") Integer userNo);
	
	// --------------------------
	void updateUserMileageByBuy(PayRequest payRequest);
	
	int getUserMileage(Integer userNo);
    
	void insertOrderGroup(BuyOrderGroup orderGroup);
	
	int getOrderId(Integer userNo);

	void insertOrderList(@Param("productList") List<ProductWithCouponResponse> items, @Param("orderId") int orderId,
			@Param("userNo") Integer userNo);
	
	void updateCancelStockHold(@Param("productList") List<ProductWithCouponResponse> items);

	void updateProductOptionByBuy(@Param("productList") List<ProductWithCouponResponse> items);

	void updateUserCouponUsed(@Param("productList") List<ProductWithCouponResponse> items, @Param("userCouponId") int userCouponId);

	void updateCommonCouponByBuy(@Param("userCouponId") int userCouponId);

	void updateEachCouponByBuy(@Param("productList") List<ProductWithCouponResponse> items);

	

	

	

	
	

}
