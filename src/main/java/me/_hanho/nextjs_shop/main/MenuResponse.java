package me._hanho.nextjs_shop.main;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private int menuTopId;
    private String menuName;
    private String gender;
    
    private List<MenuSubDTO> menuSubList;

}
