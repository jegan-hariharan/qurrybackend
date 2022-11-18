package com.quarry.management.DTO;

import lombok.Data;

@Data
public class AdminReq {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
}
