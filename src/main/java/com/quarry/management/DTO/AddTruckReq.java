package com.quarry.management.DTO;

import lombok.Data;

@Data
public class AddTruckReq {

    private Long quarryId;
    private Long truckOwnerId;
    private String truckOwnerName;
    private String truckOwnerMobileNo;
    private String truckOwnerAddress;
    private String truckNo;
    private Float truckCapacity;
    private String truckDriverName;
    private String truckDriverMobile;
}
