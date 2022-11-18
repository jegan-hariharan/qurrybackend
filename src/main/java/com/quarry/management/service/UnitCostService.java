package com.quarry.management.service;

import com.quarry.management.DTO.UnitCostRequestDTO;
import com.quarry.management.DTO.UnitCostResponseDTO;
import com.quarry.management.entity.UnitCost;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UnitCostService {
    public UnitCost saveUnitCost(UnitCostRequestDTO unitCost);

    List<UnitCost> fetchAllUnits();

    ResponseEntity<List<UnitCostResponseDTO>> fetchProductUnits(Long quarryId, Long productId);

    Optional<UnitCost> calcualteLoadAmt(Long quarryId, Long productId);
}
