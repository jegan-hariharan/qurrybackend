package com.quarry.management.service;

import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.DTO.UnitCostRequestDTO;
import com.quarry.management.DTO.UnitCostResponseDTO;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Product;
import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.UnitCost;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.exception.RecordNotFoundException;
import com.quarry.management.repository.ProductRepository;
import com.quarry.management.repository.QuarryRespository;
import com.quarry.management.repository.UnitCostRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UnitCostServiceImpl implements UnitCostService {

    @Autowired
    private UnitCostRespository unitCostRespository;

    @Autowired
    private QuarryRespository quarryRespository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public UnitCost saveUnitCost(UnitCostRequestDTO unitCostRequestDTO) {
        UnitCost unitCost = new UnitCost();
        unitCost.setQuarry(findQuarryById(unitCostRequestDTO.getQuarryId()));
        unitCost.setProduct(findProductById(unitCostRequestDTO.getProductId()));
        try {
            unitCost.setCreatedDate(new Timestamp(new Date().getTime()));
            unitCost.setUnitCost(unitCostRequestDTO.getUnitCost());
            unitCost.setUnit(unitCostRequestDTO.getUnit());
            return unitCostRespository.save(unitCost);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

    }

    private Product findProductById(Long productId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if(!existingProduct.isPresent()) {
            throw new RecordNotFoundException("Product Not Found");
        }
        return existingProduct.get();
    }

    private Quarry findQuarryById(Long quarryId) {
        Optional<Quarry> existingQuarry = quarryRespository.findById(quarryId);
        if(!existingQuarry.isPresent()) {
            throw new RecordNotFoundException("Minerals Not Found");
        }
        return existingQuarry.get();
    }

    @Override
    public List<UnitCost> fetchAllUnits() {
        return unitCostRespository.findAllByOrderByUnitIdDesc();
    }

    @Override
    public Optional<UnitCost> calcualteLoadAmt(Long quarryId, Long productId) {
        return unitCostRespository.findTop1ByQuarryAndProductOrderByUnitIdDesc(findQuarryById(quarryId), findProductById(productId));
    }

    @Override
    public ResponseEntity<List<UnitCostResponseDTO>> fetchProductUnits(Long quarryId, Long productId) {
        List<UnitCostResponseDTO> UnitCostResponseDTOs = new ArrayList<>();
        List<UnitCost> unitCostList = unitCostRespository.findByQuarryAndProductOrderByUnitIdDesc(quarryRespository.findById(quarryId).get(), productRepository.findById(productId).get());
        if (!unitCostList.isEmpty()) {
            unitCostList.forEach(cost -> UnitCostResponseDTOs.add(new UnitCostResponseDTO(cost)));
        }
        return ResponseEntity.ok(UnitCostResponseDTOs);
    }

}
