package com.quarry.management.DTO;

import com.quarry.management.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAdminResDTO {

    private Long adminId;

    private String accessToken;

    private String type = "Bearer";

    private String role;

    private Integer roleId;

    private String adminName;

    public AuthAdminResDTO(Users user, String token) {
        this.adminId = user.getAdmin().getAdminId();
        this.accessToken = token;
        this.role = user.getRoles().get(0).getName();
        this.roleId = user.getRoles().get(0).getRoleId();
        this.adminName = user.getAdmin().getName();
    }
}