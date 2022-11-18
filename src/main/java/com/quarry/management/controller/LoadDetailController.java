package com.quarry.management.controller;

import com.quarry.management.DTO.ChartResponse;
import com.quarry.management.DTO.ProductLoadUnitChart;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Product;
import com.quarry.management.service.LoadDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoadDetailController {

    @Autowired
    private LoadDetailService loadDetailService;


    @GetMapping("/getAllLoads/{quarryId}")
    public ResponseEntity<List<LoadResponseDTO>> fetchAllLoads(@PathVariable("quarryId") Long quarryId) {
        return loadDetailService.fetchAllLoads(quarryId);
    }

    @GetMapping("/admin/chart_values")
    public ResponseEntity<ChartResponse> getChartValues() {
        return loadDetailService.getChartValues();
    }

    @GetMapping("/admin/editPaymentStatus/{loadId}")
    public LoadDetail updateProductStatusById(@PathVariable("loadId") Long loadId) {
        return loadDetailService.updatePaymentStatusById(loadId);
    }

}
