package com.quarry.management.service;

import com.quarry.management.DTO.ChallanCostRequestDTO;
import com.quarry.management.entity.ChallanCost;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.repository.ChallanCostRespository;
import com.quarry.management.repository.QuarryRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChallanCostServiceImpl implements ChallanCostService{

    @Autowired
    private ChallanCostRespository challanCostRespository;

    @Autowired
    private QuarryRespository quarryRespository;

    @Override
    public List<ChallanCost> fetchChallanCost(Long quarryId) {
        return challanCostRespository.findByQuarryOrderByChallanIdDesc(quarryRespository.findById(quarryId).get());
    }

    @Override
    public ChallanCost insertChallanCost(ChallanCostRequestDTO challanCostRequestDTO) {
        try {
            ChallanCost challanCost = new ChallanCost();
            challanCost.setChallanCost(challanCostRequestDTO.getChallanCost());
            challanCost.setCreatedDate(new Timestamp(new Date().getTime()));
            challanCost.setQuarry(quarryRespository.findById(challanCostRequestDTO.getQuarryId()).get());
            challanCost = challanCostRespository.save(challanCost);
            return challanCost;
        } catch(Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public Optional<ChallanCost> getChallanUpdatedCost(Long quarryId) {
        return challanCostRespository.findTop1ByQuarryOrderByChallanIdDesc(quarryRespository.findById(quarryId).get());
    }
}
