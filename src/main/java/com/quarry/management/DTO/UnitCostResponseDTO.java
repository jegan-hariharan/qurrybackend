package com.quarry.management.DTO;

import com.quarry.management.entity.UnitCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitCostResponseDTO {
    private Float unitCost;
    private Timestamp createdDate;
    private Long productId;
    private String productName;

    public UnitCostResponseDTO(UnitCost cost) {
        this.unitCost = cost.getUnitCost();
        this.createdDate = cost.getCreatedDate();
        this.productId = cost.getProduct().getProductId();
        this.productName = cost.getProduct().getProductName();
    }
}
