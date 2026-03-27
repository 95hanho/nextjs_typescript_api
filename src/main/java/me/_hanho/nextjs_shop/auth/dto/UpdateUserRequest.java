package me._hanho.nextjs_shop.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank private String zonecode;
    @NotBlank private String address;
    @NotBlank private String addressDetail;
    @NotBlank private String phone;
    @NotBlank private String email;
}
