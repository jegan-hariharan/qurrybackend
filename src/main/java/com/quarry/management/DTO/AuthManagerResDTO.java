package com.quarry.management.DTO;

import com.quarry.management.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthManagerResDTO {
    private Long managerId;

    private String accessToken;


    private String type = "Bearer";

    private String role;

    private Integer roleId;

    private Long quarryId;

    public AuthManagerResDTO(Users user, String token) {
        this.managerId = user.getManager().getId();
        this.accessToken = token;
        this.role = user.getRoles().get(0).getName();
        this.roleId = user.getRoles().get(0).getRoleId();
        this.quarryId = user.getManager().getQuarry().getQuarryId();
    }
}
