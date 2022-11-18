package com.quarry.management.DTO;

import com.quarry.management.entity.LoadDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadResponseDTO {

    private String adminPhone;
    private Long truckOwnerId;
    private String truckOwnerName;
    private String truckOwnerMobileNo;
    private String truckOwnerAddress;
    private String truckDriverName;
    private String truckDriveMobileNo;
    private String truckVehicleNo;

    private Float truckCapacity;
    private Timestamp truckInTime;
    private Timestamp truckOutTime;
    private Timestamp loadDate;
    private Float loadAmt;
    private Float loadUnit;
    private Boolean entryStatus;
    private Boolean loadStatus;

    private Long loadId;

    private String productName;

    private Float totalAmt;

    private Boolean paymentStatus;

    private Boolean paymentType;

    private Long totalChallan;

    private Float totalChallanAmt;

    private Float challanUnitCost;

    private Float productUnitCost;

    public LoadResponseDTO(LoadDetail load) {
        this.adminPhone = load.getQuarry().getAdmin().getPhone();
        this.truckOwnerId = load.getTruckOwner().getUserId();
        this.truckOwnerName = load.getTruckOwner().getTruckOwnerName();
        this.truckOwnerMobileNo = load.getTruckOwner().getTruckMobileNo();
        this.truckOwnerAddress = load.getTruckOwner().getTruckOwnerAddress();
        this.truckDriverName = load.getTruckDriverDetail().getDriverName();
        this.truckDriveMobileNo = load.getTruckDriverDetail().getDriverMobileNo();
        this.truckVehicleNo = load.getTruckDetail().getTruckNo();
        this.truckCapacity = load.getTruckDetail().getTruckCapacity();
        this.truckInTime = load.getVehicleInTime();
        this.truckOutTime = load.getVehicleOutTime();
        this.loadDate = load.getLoadDate();
        this.loadAmt = load.getLoadAmt();
        this.loadUnit = load.getLoadUnit();
        this.loadStatus = load.getLoadStatus();
        this.entryStatus = load.getEntryStatus();
        this.loadId = load.getLoadId();
        this.productName = load.getProduct().getProductName();
        this.totalAmt = load.getTotalAmt();
        this.paymentStatus = load.getPaymentStatus();
        this.paymentType = load.getPaymentType();
        this.totalChallan = load.getTotalChallan();
        this.totalChallanAmt = load.getTotalChallanAmt();
        this.challanUnitCost = load.getChallanUnitCost();
        this.productUnitCost = load.getProductUnitCost();
    }
}