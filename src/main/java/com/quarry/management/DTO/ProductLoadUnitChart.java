package com.quarry.management.DTO;

import com.quarry.management.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLoadUnitChart {

    private Long productId;

    private String productName;

    private double count;

    public ProductLoadUnitChart(Product product, double count) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.count = count;
    }
}
