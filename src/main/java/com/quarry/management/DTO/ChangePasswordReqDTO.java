package com.quarry.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordReqDTO {

    //@NotNull(message = "User Id field should be not empty")
    private Long userId;

    //@NotBlank(message = "Current password field should be not empty")
    private String currentPassword;

    //@NotBlank(message = "New password field should be not empty")
    private String newPassword;
}