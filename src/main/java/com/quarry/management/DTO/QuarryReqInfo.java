package com.quarry.management.DTO;

import lombok.Data;

import java.util.Date;


@Data
public class QuarryReqInfo {

    private Long quaryId;
    private String quarryName;
    private String quarryOwnerName;
    private String quarryMobileNumber;
    private String quarryAddress;
    private String quarryEmail;
    private String quarryGstn;
    private String quarryLicense;
    private Date quarryValidity;
    private Long adminId;

    private String employeeName;
    private String email;
    private String phone;
    private String password;
}
