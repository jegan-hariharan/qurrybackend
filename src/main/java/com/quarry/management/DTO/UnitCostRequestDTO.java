package com.quarry.management.DTO;

import lombok.Data;

@Data
public class UnitCostRequestDTO {

    private Float unit;
    private Float unitCost;
    private Long quarryId;
    private Long productId;
}
