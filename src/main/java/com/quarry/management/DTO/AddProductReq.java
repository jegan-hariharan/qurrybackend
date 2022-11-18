package com.quarry.management.DTO;

import lombok.Data;

@Data
public class AddProductReq {
    private String productName;
    private Float unitCost;
    private Long quarryId;
    private Boolean isActive;
}
