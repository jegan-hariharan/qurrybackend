package com.quarry.management.DTO;

import com.quarry.management.entity.TruckDetail;
import com.quarry.management.entity.TruckDriverDetail;
import com.quarry.management.entity.TruckOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckInfoResDTO {
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

    public TruckInfoResDTO(TruckDetail truckDetail, TruckDriverDetail truckDriverDetail) {
        this.truckId = truckDetail.getTruckId();
        this.truckNo = truckDetail.getTruckNo();
        this.truckOwnerName = truckDetail.getTruckOwner().getTruckOwnerName();
        this.truckOwnerMobileNo = truckDetail.getTruckOwner().getTruckMobileNo();
        this.truckOwnerAddress = truckDetail.getTruckOwner().getTruckOwnerAddress();
        this.truckDriverName = truckDriverDetail.getDriverName();
        this.truckDriverMobileNo = truckDriverDetail.getDriverMobileNo();
        this.ownerId = truckDetail.getTruckOwner().getUserId();
        this.driverId = truckDriverDetail.getDriverId();
        this.quarryId = truckDetail.getQuarry().getQuarryId();
    }

    public TruckInfoResDTO(TruckOwner truckOwner) {
        this.truckOwnerName = truckOwner.getTruckOwnerName();
        this.ownerId = truckOwner.getUserId();
    }
}
