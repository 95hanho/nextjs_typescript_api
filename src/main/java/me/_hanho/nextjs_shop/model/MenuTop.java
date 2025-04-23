package me._hanho.nextjs_shop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTop {
    private int menu_top_id;
    private String menu_name;
    private String gender;
    private List<MenuSub> subMenus;
}
