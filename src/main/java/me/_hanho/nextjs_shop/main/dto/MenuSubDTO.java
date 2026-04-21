package me._hanho.nextjs_shop.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuSubDTO {
    private int menuSubId;
    private String menuName;
    private int productCount;

}
