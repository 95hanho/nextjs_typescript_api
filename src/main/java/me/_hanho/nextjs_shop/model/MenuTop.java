package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTop {
    private int menuTopId;
    private String menuName;
    private String gender; // 'M','F'
}
