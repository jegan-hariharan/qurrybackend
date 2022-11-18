package com.quarry.management.service;

import com.quarry.management.DTO.ChallanCostRequestDTO;
import com.quarry.management.entity.ChallanCost;

import java.util.List;
import java.util.Optional;

public interface ChallanCostService {
    List<ChallanCost> fetchChallanCost(Long quarryId);

    public ChallanCost insertChallanCost(ChallanCostRequestDTO challanCostRequestDTO);

    Optional<ChallanCost> getChallanUpdatedCost(Long quarryId);
}
