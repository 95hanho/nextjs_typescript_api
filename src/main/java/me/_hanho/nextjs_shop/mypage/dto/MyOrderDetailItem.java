package me._hanho.nextjs_shop.mypage.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDetailItem {
	
    private int orderItemId;
    private int orderId;
    private int holdId;
    private String productName;
    private int count;
    private String size;
    private int originPrice;
    private int finalPrice;
    private int addPrice;
    private BigDecimal couponDiscountedPrice;
    private BigDecimal totalPrice;
    private String status; // 상태값 'ORDERED','CANCELLED','SHIPPED','DELIVERED','PREPARING'
    private Timestamp shippingDate; // 발송일자
    private Timestamp deliveredDate; // 배송완료일자
    private Timestamp returnDate; // 반송일자

    private int productId;

    private int reviewId;

    private int sellerNo;
    private String sellerName;
    private String sellerNameEn;
    
    private int productImageId;
    private int fileId;
    private String fileName;
    private String storeName;
    private String filePath;
    private String copyright;
    private String copyrightUrl;
    private String fileExtension;

    private List<MyOrderDetailItemCoupon> coupons;
}
