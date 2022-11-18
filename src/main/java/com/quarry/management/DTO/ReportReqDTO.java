package com.quarry.management.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class ReportReqDTO {
    private Long productId;
    private Long ownerId;
    private Boolean paymentType;
    private Boolean paymentStatus;
    private Long quarryId;
    private Date startDate;
    private Date endDate;
    private Long truckId;
}
