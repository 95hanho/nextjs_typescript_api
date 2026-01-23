package me._hanho.nextjs_shop.auth;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
	private Integer userNo;
    @NotBlank private String userId;
    @NotBlank private String password;
    @NotBlank private String name;
    @NotBlank private String zonecode;
    @NotBlank private String address;
    @NotBlank private String addressDetail;
    @NotNull private Timestamp birthday;
    @NotBlank private String phone;
    @NotBlank private String email;
}