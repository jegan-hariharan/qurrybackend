package com.quarry.management.controller;

import com.quarry.management.DTO.UnitCostRequestDTO;
import com.quarry.management.DTO.UnitCostResponseDTO;
import com.quarry.management.entity.UnitCost;
import com.quarry.management.service.UnitCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UnitCostController {

    @Autowired
    private UnitCostService unitCostService;

    @PostMapping("/admin/unitcostinfo")
    public UnitCost saveUnitCost(@RequestBody UnitCostRequestDTO unitCostRequestDTO){
        return unitCostService.saveUnitCost(unitCostRequestDTO);
    }

    @GetMapping("/admin/productUnits/{quarryId}/{productId}")
    public ResponseEntity<List<UnitCostResponseDTO>> fetchProductUnits(@PathVariable("quarryId") Long quarryId, @PathVariable("productId") Long productId) {
        return unitCostService.fetchProductUnits(quarryId, productId);
    }

    @GetMapping("/calcualteLoadAmt/{quarryId}/{productId}")
    public Optional<UnitCost> calcualteLoadAmt(@PathVariable("quarryId") Long quarryId, @PathVariable("productId") Long productId) {
        return unitCostService.calcualteLoadAmt(quarryId, productId);
    }
}
