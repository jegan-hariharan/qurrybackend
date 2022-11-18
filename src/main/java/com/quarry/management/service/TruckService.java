package com.quarry.management.service;

import com.quarry.management.DTO.AddTruckReq;
import com.quarry.management.DTO.LoadReqDTO;
import com.quarry.management.DTO.SearchTruckResDTOList;
import com.quarry.management.DTO.TruckInfoResDTO;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.TruckOwner;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TruckService {
    ResponseEntity<Object> addOwnerVehicleDriverDetails(Long quarryId, AddTruckReq addTruckReq);

    ResponseEntity<TruckInfoResDTO> fetchTruckById(Long truckId, Long quarryId);

    ResponseEntity<List<SearchTruckResDTOList>> fetchTruckByNo(String truckNo, Long quarryId);

    ResponseEntity<LoadDetail> generateTruckEntryPass(LoadReqDTO loadReqDTO);

    ResponseEntity<List<LoadDetail>> fetchEnteredTruckByNo(String truckNo);

    LoadDetail fetchLoadDetailsById(Long loadId);

    LoadDetail updateTruckExitStatus(Long loadId);

    LoadDetail updateLoadDetail(Long loadId, LoadReqDTO loadReqDTO);

    ResponseEntity<List<TruckInfoResDTO>> getAllOwner(Long quarryId);

    ResponseEntity<Object> addVehicleOwner(AddTruckReq addTruckReq);
}
