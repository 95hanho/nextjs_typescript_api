package me._hanho.nextjs_shop.admin;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Seller;

@Mapper
public interface AdminMapper {

	void addSeller(Seller seller);

}
