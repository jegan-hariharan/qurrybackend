package com.quarry.management.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LoadReqDTO {
    private Long loadId;
    private Long quarryId;
    private Long ownerId;
    private Long truckId;
    private Long driverId;
    private Long productId;
    private Boolean entryStatus;
    private Boolean loadStatus;
    private Timestamp truckInTime;
    private Timestamp truckOutTime;
    private Float loadUnit;
    private Float loadAmt;
    private Timestamp loadedAt;
    private Float productUnitCost;
    private Float challanUnitCost;
    private Boolean isChallan;
    private Long totalChallan;
    private Float totalChallanAmt;
    private Boolean paymentType;
    private Boolean paymentStatus;
    private Float totalAmt;
}
