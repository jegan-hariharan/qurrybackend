package com.quarry.management.DTO;


import com.quarry.management.entity.Users;
import lombok.Data;

@Data
public class AuthEmployeeResDTO {

    private Long employeeId;

    private String accessToken;


    private String type = "Bearer";

    private String role;

    private Integer roleId;

    private Long quarryId;

    public AuthEmployeeResDTO(Users user, String token) {
        this.employeeId = user.getEmployee().getEmployeeId();
        this.accessToken = token;
        this.role = user.getRoles().get(0).getName();
        this.roleId = user.getRoles().get(0).getRoleId();
        this.quarryId = user.getEmployee().getQuarry().getQuarryId();
    }
}