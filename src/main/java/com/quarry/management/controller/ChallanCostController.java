package com.quarry.management.controller;

import com.quarry.management.DTO.ChallanCostRequestDTO;
import com.quarry.management.DTO.QuarryReqInfo;
import com.quarry.management.entity.ChallanCost;
import com.quarry.management.entity.UnitCost;
import com.quarry.management.service.ChallanCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChallanCostController {

    @Autowired
    private ChallanCostService challanCostService;

    @GetMapping("/admin/getChallanCost/{quarryId}")
    public List<ChallanCost> fetchChallanCost(@PathVariable("quarryId") Long quarryId) {
        return challanCostService.fetchChallanCost(quarryId);
    }

    @PostMapping("/admin/challanCostInfo")
    public ChallanCost insertChallanCost(@RequestBody ChallanCostRequestDTO challanCostRequestDTO) {
        return challanCostService.insertChallanCost(challanCostRequestDTO);
    }

    @GetMapping("/admin/getChallanUpdatedCost/{quarryId}")
    public Optional<ChallanCost> getChallanUpdatedCost(@PathVariable("quarryId") Long quarryId) {
        return challanCostService.getChallanUpdatedCost(quarryId);
    }
}
