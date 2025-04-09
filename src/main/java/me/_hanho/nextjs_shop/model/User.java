package me._hanho.nextjs_shop.model;

import java.util.Date;

public class User {

	private String id;
	private String password;
	private String password_check;
	private String name;
	private String zonecode;
	private String address;
	private String birthday;
	private String phone;
	private String email;
	
	private Date created_at;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String id, String password, String password_check, String name, String zonecode, String address,
			String birthday, String phone, String email, Date created_at) {
		super();
		this.id = id;
		this.password = password;
		this.password_check = password_check;
		this.name = name;
		this.zonecode = zonecode;
		this.address = address;
		this.birthday = birthday;
		this.phone = phone;
		this.email = email;
		this.created_at = created_at;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword_check() {
		return password_check;
	}

	public void setPassword_check(String password_check) {
		this.password_check = password_check;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZonecode() {
		return zonecode;
	}

	public void setZonecode(String zonecode) {
		this.zonecode = zonecode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + ", password_check=" + password_check + ", name=" + name
				+ ", zonecode=" + zonecode + ", address=" + address + ", birthday=" + birthday + ", phone=" + phone
				+ ", email=" + email + ", created_at=" + created_at + "]";
	}
	
	
}
