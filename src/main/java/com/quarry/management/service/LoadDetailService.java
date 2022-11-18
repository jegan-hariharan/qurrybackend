package com.quarry.management.service;

import com.quarry.management.DTO.ChartResponse;
import com.quarry.management.DTO.ProductLoadUnitChart;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoadDetailService {

    ResponseEntity<List<LoadResponseDTO>> fetchAllLoads(Long quarryId);

    ResponseEntity<ChartResponse> getChartValues();

    LoadDetail updatePaymentStatusById(Long productId);
}
