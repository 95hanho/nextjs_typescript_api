package me._hanho.nextjs_shop.mypage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDetailItemDTO {
	
    private int orderListId;
    private int orderId;
    private int count;
    private BigDecimal orderPrice;
    private BigDecimal discountPrice;
    private BigDecimal finalPrice;
    
	// 해당 제품에 사용한 쿠폰 내역
	private int usercouponId;
	private int couponId;
	private String description;
	private String couponCode;
	private String discountType;
	private BigDecimal discountValue;
	private BigDecimal maxDiscount;
	private BigDecimal minimumOrderBeforeAmount;
	private boolean isStackable;

    private int holdId;
    private int product_detail_id;
    private int add_price;
    private String size;
    
    private int product_id;
    private String productName;
    private String color_name;
    private int price;
    
    private String seller_id;
    private String seller_name;
    
    private int review_id;
    private String content;
    private Timestamp reviewDate;
    private int rating;
    
    private int menu_sub_id;
    private String sub_menu_name;
    private int menu_top_id;
    private String top_menu_name;
    
    private int product_image_id;
    private int file_id;
    private String file_name;
    private String store_name;
    private String file_extension;
    private String file_path;
    private String copyright;
    private String copyright_url;
}
