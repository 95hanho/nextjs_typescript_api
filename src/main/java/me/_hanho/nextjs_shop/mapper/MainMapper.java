package me._hanho.nextjs_shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Product;

@Mapper
public interface MainMapper {

	List<Product> getMainImages();

}
