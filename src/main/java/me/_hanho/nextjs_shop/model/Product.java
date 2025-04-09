package me._hanho.nextjs_shop.model;

import java.util.Date;

public class Product {

	private int product_id;
	private String name;
	private String brand;
	private int price;
	private String img_path;
	private String copyright;
	private Date created_at;
	private int view_count;
	private int wish_count;
	private int sales_count;
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Product(int product_id, String name, String brand, int price, String img_path, String copyright,
			Date created_at, int view_count, int wish_count, int sales_count) {
		super();
		this.product_id = product_id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.img_path = img_path;
		this.copyright = copyright;
		this.created_at = created_at;
		this.view_count = view_count;
		this.wish_count = wish_count;
		this.sales_count = sales_count;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public int getWish_count() {
		return wish_count;
	}

	public void setWish_count(int wish_count) {
		this.wish_count = wish_count;
	}

	public int getSales_count() {
		return sales_count;
	}

	public void setSales_count(int sales_count) {
		this.sales_count = sales_count;
	}

	@Override
	public String toString() {
		return "Product [product_id=" + product_id + ", name=" + name + ", brand=" + brand + ", price=" + price
				+ ", img_path=" + img_path + ", copyright=" + copyright + ", created_at=" + created_at + ", view_count="
				+ view_count + ", wish_count=" + wish_count + ", sales_count=" + sales_count + "]";
	}
	
}
