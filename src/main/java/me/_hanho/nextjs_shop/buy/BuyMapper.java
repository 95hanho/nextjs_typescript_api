package me._hanho.nextjs_shop.buy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.OrderGroup;
import me._hanho.nextjs_shop.model.StockHold;

@Mapper
public interface BuyMapper {
	
	List<StockHold> selectAllActiveHoldsByUser(String userId);
	
    // 전역 가용수량 (stock - 모든 유저 미만료 HOLD 합)
    java.util.List<AvailabilityRow> selectAvailability(@Param("detailIds") java.util.List<Integer> detailIds);

    // 본인 미만료 HOLD (있으면 재사용/덮어쓰기)
    java.util.List<ExistingHold> selectExistingHolds(@Param("userId") String userId,
                                                     @Param("detailIds") java.util.List<Integer> detailIds);

    // 멱등 업서트 (유니크 충돌 시 count/expiresAt 갱신)
    int upsertHolds(@Param("rows") java.util.List<UpsertHoldRow> rows);

    // 하트비트용 holdId 매핑
    java.util.List<HoldBrief> selectLatestHolds(@Param("userId") String userId,
                                                @Param("detailIds") java.util.List<Integer> detailIds);

    int extendHolds(@Param("holdIds") List<Integer> holdIds,
            @Param("ttlSeconds") int ttlSeconds);

    int releaseHolds(@Param("holdIds") List<Integer> holdIds);
    
	List<OrderStockDTO> getOrderStock(String userId);
	
	List<AvailableCoupon> getAvailableCoupon(@Param("productIds") List<Integer> productIds, @Param("userId") String userId);
	
	List<ProductWithCouponsDTO> getProductWithCoupons(@Param("products") List<BuyProduct> products, @Param("userId") String userId);
	
	// --------------------------
	void updateUserMileageByBuy(PayRequest payRequest);
	
	int getUserMileage(String userId);
    
	void insertOrderGroup(OrderGroup orderGroup);
	
	int getOrderId(String userId);

	void insertOrderList(@Param("productList") List<ProductWithCouponsDTO> items, @Param("orderId") int orderId,
			@Param("userId") String userId);
	
	void updateCancelStockHold(@Param("productList") List<ProductWithCouponsDTO> items);

	void updateProductOptionByBuy(@Param("productList") List<ProductWithCouponsDTO> items);

	void updateUserCouponUsed(@Param("productList") List<ProductWithCouponsDTO> items, @Param("userCouponId") int userCouponId);

	void updateCommonCouponByBuy(@Param("userCouponId") int userCouponId);

	void updateEachCouponByBuy(@Param("productList") List<ProductWithCouponsDTO> items);

	

	

	

	
	

}
