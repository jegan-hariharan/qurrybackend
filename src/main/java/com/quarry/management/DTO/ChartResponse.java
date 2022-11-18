package com.quarry.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponse {

    private List<ProductLoadUnitChart> productLoadUnitChartList;

    private List<ProductTotalAmtChart> productTotalAmtChartList;

}
