package com.quarry.management.service;

import com.quarry.management.DTO.ChartResponse;
import com.quarry.management.DTO.ProductLoadUnitChart;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.DTO.ProductTotalAmtChart;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Product;
import com.quarry.management.entity.Quarry;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.exception.RecordNotFoundException;
import com.quarry.management.repository.LoadDetailRepository;
import com.quarry.management.repository.QuarryRespository;
import com.quarry.management.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoadDetailServiceImpl implements LoadDetailService {

    @Autowired
    private LoadDetailRepository loadDetailRepository;

    @Autowired
    private QuarryRespository quarryRespository;

    @Override
    public ResponseEntity<List<LoadResponseDTO>> fetchAllLoads(Long quarryId) {
        List<LoadResponseDTO> loadResponseDTOS = new ArrayList<>();
        List<LoadDetail> loadDetailList =loadDetailRepository.findByQuarryOrderByLoadIdDesc(findQuarryById(quarryId));
        if (!loadDetailList.isEmpty()) {
            loadDetailList.forEach(load -> loadResponseDTOS.add(new LoadResponseDTO(load)));
        }
        return ResponseEntity.ok(loadResponseDTOS);
    }

    @Override
    public ResponseEntity<ChartResponse> getChartValues() {
        List<ProductLoadUnitChart> productLoadUnitChartList;
        List<ProductTotalAmtChart> productTotalAmtChartList;
        Date startDate = DateUtils.getPreviousDate(new Date(), -7);
        Date endDate = new Date();
        productLoadUnitChartList = loadDetailRepository.findProductLoadUnitByLoadDateBetween(startDate, endDate);
        productTotalAmtChartList = loadDetailRepository.findProductTotalAmtByLoadDateBetween(startDate, endDate);
        return ResponseEntity.ok(new ChartResponse(productLoadUnitChartList, productTotalAmtChartList));
    }

    @Override
    public LoadDetail updatePaymentStatusById(Long loadId) {
        LoadDetail loadDB = loadDetailRepository.findById(loadId).get();
        try {
            loadDB.setPaymentStatus(true);
            loadDB.setPaymentDate(new Timestamp(new Date().getTime()));
            return loadDetailRepository.save(loadDB);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    private Quarry findQuarryById(Long quarryId) {
        Optional<Quarry> existingQuarry = quarryRespository.findById(quarryId);
        if(!existingQuarry.isPresent()) {
            throw new RecordNotFoundException("Quarry Not Found");
        }
        return existingQuarry.get();
    }
}
