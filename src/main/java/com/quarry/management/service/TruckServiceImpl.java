package com.quarry.management.service;

import com.quarry.management.DTO.AddTruckReq;
import com.quarry.management.DTO.LoadReqDTO;
import com.quarry.management.DTO.SearchTruckResDTOList;
import com.quarry.management.DTO.TruckInfoResDTO;
import com.quarry.management.entity.*;
import com.quarry.management.exception.BadRequestException;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.exception.RecordNotFoundException;
import com.quarry.management.repository.*;
import com.quarry.management.utils.ResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TruckServiceImpl implements TruckService{

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckServiceImpl.class);

    @Autowired
    private TruckOwnerRespository truckOwnerRespository;
    @Autowired
    private TruckDetailRespository truckDetailRespository;
    @Autowired
    private TruckDriverRespository truckDriverRespository;
    @Autowired
    private QuarryRespository quarryRespository;

    @Autowired
    private LoadDetailRepository loadDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseEntity<Object> addOwnerVehicleDriverDetails(Long quarryId, AddTruckReq addTruckReq) {
        Optional<TruckDetail> truck = truckDetailRespository.findByTruckNo(addTruckReq.getTruckNo());
        if(truck.isPresent()) {
            throw new BadRequestException(ResponseMessages.VEHICLE_ALREADY_EXISTS);
        }
        Optional<TruckDriverDetail> truckDriver = truckDriverRespository.findByDriverMobileNo(addTruckReq.getTruckDriverMobile());
        if(truckDriver.isPresent()) {
            throw new BadRequestException(ResponseMessages.DRIVER_MOBILE_ALREADY_EXISTS);
        }
        try {
            TruckDetail truckDetail = new TruckDetail();
            truckDetail.setTruckNo(addTruckReq.getTruckNo());
            truckDetail.setTruckCapacity(addTruckReq.getTruckCapacity());
            truckDetail.setCreatedDate(new Timestamp(new Date().getTime()));
            truckDetail.setTruckOwner(findTruckOwnerById(addTruckReq.getTruckOwnerId()));
            truckDetail.setQuarry(findQuarryById(quarryId));
            truckDetail = truckDetailRespository.save(truckDetail);

            TruckDriverDetail truckDriverDetail = new TruckDriverDetail();
            truckDriverDetail.setDriverName(addTruckReq.getTruckDriverName());
            truckDriverDetail.setDriverMobileNo(addTruckReq.getTruckDriverMobile());
            truckDriverDetail.setCreatedDate(new Timestamp(new Date().getTime()));
            truckDriverDetail.setTruckDetail(truckDetail);
            truckDriverDetail = truckDriverRespository.save(truckDriverDetail);
            return ResponseEntity.ok(truckDetail);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public ResponseEntity<TruckInfoResDTO> fetchTruckById(Long truckId, Long quarryId) {
        TruckDetail truckDetail = truckDetailRespository.findById(truckId).get();
        TruckDriverDetail truckDriverDetail = truckDriverRespository.findByTruckDetail(truckDetail);

        return ResponseEntity.ok(new TruckInfoResDTO(truckDetail, truckDriverDetail));
    }

    @Override
    public ResponseEntity<List<SearchTruckResDTOList>> fetchTruckByNo(String truckNo, Long quarryId) {
        List<SearchTruckResDTOList> searchTruckResDTOLists = new ArrayList<>();
        List<TruckDetail> truckDetailList = new ArrayList<>();
        Quarry quarry = findQuarryById(quarryId);
        List<TruckOwner> truckOwners = truckOwnerRespository.findByQuarryAndTruckMobileNoContaining(quarry, truckNo);
        if(!truckOwners.isEmpty()) {
            truckDetailList.addAll(truckDetailRespository.findByTruckOwnerInAndQuarryAndEntryStatus(truckOwners, quarry, true));
        }

        truckDetailList.addAll(truckDetailRespository.findByTruckNoStartsWithAndQuarryAndEntryStatus(truckNo, quarry, true));
        if (!truckDetailList.isEmpty()) {
            truckDetailList.forEach(truck -> searchTruckResDTOLists.add(new SearchTruckResDTOList(truck)));
        }
        return ResponseEntity.ok(searchTruckResDTOLists);
    }

    @Override
    public ResponseEntity<LoadDetail> generateTruckEntryPass(LoadReqDTO loadReqDTO) {
        LoadDetail loadDetail = new LoadDetail();
        try{
            loadDetail.setQuarry(findQuarryById(loadReqDTO.getQuarryId()));
            loadDetail.setTruckOwner(findTruckOwnerById(loadReqDTO.getOwnerId()));
            loadDetail.setTruckDetail(findTruckDetailById(loadReqDTO.getTruckId()));
            loadDetail.setTruckDriverDetail(findTruckDriverDetailById(loadReqDTO.getDriverId()));
            loadDetail.setLoadStatus(loadReqDTO.getLoadStatus());
            loadDetail.setEntryStatus(loadReqDTO.getEntryStatus());
            loadDetail.setVehicleInTime(new Timestamp(new Date().getTime()));
            loadDetail.setCreatedDate(new Timestamp(new Date().getTime()));
            loadDetail = loadDetailRepository.save(loadDetail);

            TruckDetail truckDetail = truckDetailRespository.findById(loadReqDTO.getTruckId()).get();
            truckDetail.setEntryStatus(loadReqDTO.getEntryStatus());
            truckDetail = truckDetailRespository.save(truckDetail);
            return ResponseEntity.ok(loadDetail);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public ResponseEntity<List<LoadDetail>> fetchEnteredTruckByNo(String truckNo) {
        List<TruckDetail> truckDetailList = truckDetailRespository.findByTruckNoStartsWith(truckNo);
        return ResponseEntity.ok(loadDetailRepository.findByTruckDetailInAndEntryStatus(truckDetailList, false));
    }

    @Override
    public LoadDetail fetchLoadDetailsById(Long loadId) {
        return loadDetailRepository.findById(loadId).get();
    }

    @Override
    public LoadDetail updateTruckExitStatus(Long loadId) {
        LoadDetail loadDetail = loadDetailRepository.findById(loadId).get();
        try{
            loadDetail.setVehicleOutTime(new Timestamp(new Date().getTime()));
            loadDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
            loadDetail.setEntryStatus(true);
            loadDetail = loadDetailRepository.save(loadDetail);
            return loadDetail;
        }catch(Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public LoadDetail updateLoadDetail(Long loadId, LoadReqDTO loadReqDTO) {
        LoadDetail loadDetail = loadDetailRepository.findById(loadId).get();
        try {
            loadDetail.setLoadAmt(loadReqDTO.getLoadAmt());
            loadDetail.setLoadUnit(loadReqDTO.getLoadUnit());
            loadDetail.setLoadStatus(loadReqDTO.getLoadStatus());
            loadDetail.setProduct(findProductById(loadReqDTO.getProductId()));
            loadDetail.setProductUnitCost(loadReqDTO.getProductUnitCost());
            loadDetail.setChallanUnitCost(loadReqDTO.getChallanUnitCost());
            loadDetail.setIsChallan(loadReqDTO.getIsChallan());
            loadDetail.setTotalChallan(loadReqDTO.getTotalChallan());
            loadDetail.setTotalChallanAmt(loadReqDTO.getTotalChallanAmt());
            loadDetail.setTotalAmt(loadReqDTO.getTotalAmt());
            loadDetail.setPaymentType(loadReqDTO.getPaymentType());
            loadDetail.setPaymentStatus(loadReqDTO.getPaymentStatus());
            loadDetail.setEntryStatus(true);
            loadDetail.setLoadDate(new Timestamp(new Date().getTime()));
            loadDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
            loadDetail.setLoadDate(new Timestamp(new Date().getTime()));
            loadDetail = loadDetailRepository.save(loadDetail);

            TruckDetail truckDetail = loadDetail.getTruckDetail();
            truckDetail.setEntryStatus(true);
            truckDetail = truckDetailRespository.save(truckDetail);
            return loadDetail;
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    @Override
    public ResponseEntity<List<TruckInfoResDTO>> getAllOwner(Long quarryId) {
        List<TruckInfoResDTO> truckInfoResDTOS = new ArrayList<>();
        List<TruckOwner> truckOwner = truckOwnerRespository.findByQuarry(findQuarryById(quarryId));
        if (!truckOwner.isEmpty()) {
            truckOwner.forEach(truck -> truckInfoResDTOS.add(new TruckInfoResDTO(truck)));
        }
        return ResponseEntity.ok(truckInfoResDTOS);
    }

    @Override
    public ResponseEntity<Object> addVehicleOwner(AddTruckReq addTruckReq) {
        Optional<TruckOwner> owner = truckOwnerRespository.findByTruckMobileNo(addTruckReq.getTruckOwnerMobileNo());
        if (owner.isPresent()) {
            throw new BadRequestException(ResponseMessages.TRUCKOWNER_ALREADY_EXISTS);
        }
        try {
            TruckOwner truckOwner = new TruckOwner();
            truckOwner.setTruckOwnerName(addTruckReq.getTruckOwnerName());
            truckOwner.setTruckMobileNo(addTruckReq.getTruckOwnerMobileNo());
            truckOwner.setTruckOwnerAddress(addTruckReq.getTruckOwnerAddress());
            truckOwner.setCreatedDate(new Timestamp(new Date().getTime()));
            truckOwner.setQuarry(findQuarryById(addTruckReq.getQuarryId()));
            truckOwner = truckOwnerRespository.save(truckOwner);
            return ResponseEntity.ok(truckOwner);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    private Product findProductById(Long productId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if(!existingProduct.isPresent()) {
            throw new RecordNotFoundException("Product NOT Found");
        }
        return existingProduct.get();
    }

    private Quarry findQuarryById(Long quarryId) {
        Optional<Quarry> existingQuarry = quarryRespository.findById(quarryId);
        if(!existingQuarry.isPresent()) {
            throw new RecordNotFoundException("MINERALS NOT Found");
        }
        return existingQuarry.get();
    }

    private TruckOwner findTruckOwnerById(Long ownerId) {
        Optional<TruckOwner> existingOwnerId = truckOwnerRespository.findById(ownerId);
        if(!existingOwnerId.isPresent()) {
            throw new RecordNotFoundException("Owner Not Found");
        }
        return existingOwnerId.get();
    }

    private TruckDetail findTruckDetailById(Long truckId) {
        Optional<TruckDetail> existingTruckDetail = truckDetailRespository.findById(truckId);
        if(!existingTruckDetail.isPresent()) {
            throw new RecordNotFoundException("Truck Not Found");
        }
        return existingTruckDetail.get();
    }

    private TruckDriverDetail findTruckDriverDetailById(Long driverId) {
        Optional<TruckDriverDetail> existingTruckDetail = truckDriverRespository.findById(driverId);
        if(!existingTruckDetail.isPresent()) {
            throw new RecordNotFoundException("Driver Not Found");
        }
        return existingTruckDetail.get();
    }
    private TruckDetail findByTruckId(Long truckId) {
        Optional<TruckDetail> truckDetailOptional = truckDetailRespository.findById(truckId);
        if(!truckDetailOptional.isPresent()) {
            throw new RecordNotFoundException("Truck Not Found");
        }
        return truckDetailOptional.get();
    }
}
