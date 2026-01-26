package me._hanho.nextjs_shop.mypage;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemWithReviewDTO {
	
    private int orderListId;
    private int orderId;
    private int count;
    private BigDecimal orderPrice;
    private BigDecimal discountPrice;
    private BigDecimal paidUnitPrice;

    private int holdId;
    private int productOptionId;
    private int addPrice;
    private String size;
    
    private int productId;
    private String productName;
    private String colorName;
    private int originPrice;
    private int finalPrice;
    
    private String sellerName;
    
    private int reviewId;
    private String content;
    private Timestamp reviewDate;
    private int rating;
    
    private int menuSubId;
    private String subMenuName;
    private int menuTopId;
    private String topMenuName;
    private String gender;
    
    private int productImageId;
    private int fileId;
    private String fileName;
    private String storeName;
    private String fileExtension;
    private String filePath;
    private String copyright;
    private String copyrightUrl;

}
