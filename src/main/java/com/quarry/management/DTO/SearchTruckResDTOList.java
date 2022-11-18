package com.quarry.management.DTO;

import com.quarry.management.entity.TruckDetail;
import com.quarry.management.entity.TruckDriverDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTruckResDTOList {

    private Long truckId;
    private String truckNo;
    private String truckOwnerName;
    private String truckOwnerMobileNo;
    private String truckOwnerAddress;
    private String truckDriverName;
    private String truckDriverMobileNo;

    private Long ownerId;
    private Long driverId;
    private Long quarryId;

    public SearchTruckResDTOList(TruckDriverDetail truckDriverDetail) {
        this.truckId = truckDriverDetail.getTruckDetail().getTruckId();
        this.truckNo = truckDriverDetail.getTruckDetail().getTruckNo();
        this.truckOwnerName = truckDriverDetail.getTruckDetail().getTruckOwner().getTruckOwnerName();
        this.truckOwnerMobileNo = truckDriverDetail.getTruckDetail().getTruckOwner().getTruckMobileNo();
        this.truckOwnerAddress = truckDriverDetail.getTruckDetail().getTruckOwner().getTruckOwnerAddress();
        this.truckDriverName = truckDriverDetail.getDriverName();
        this.truckDriverMobileNo = truckDriverDetail.getDriverMobileNo();
        this.ownerId = truckDriverDetail.getTruckDetail().getTruckOwner().getUserId();
        this.driverId = truckDriverDetail.getDriverId();
        this.quarryId = truckDriverDetail.getTruckDetail().getQuarry().getQuarryId();
    }

    public SearchTruckResDTOList(TruckDetail truckDetail) {
        this.truckNo = truckDetail.getTruckNo();
         this.truckId = truckDetail.getTruckId();
    }
}
