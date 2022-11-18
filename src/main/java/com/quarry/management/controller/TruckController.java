package com.quarry.management.controller;

import com.quarry.management.DTO.AddTruckReq;
import com.quarry.management.DTO.LoadReqDTO;
import com.quarry.management.DTO.SearchTruckResDTOList;
import com.quarry.management.DTO.TruckInfoResDTO;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Product;
import com.quarry.management.entity.TruckDetail;
import com.quarry.management.entity.TruckOwner;
import com.quarry.management.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class TruckController {

    @Autowired
    private TruckService truckService;

    @PostMapping(value = "/admin/addVehicle/{quarryId}")
    public ResponseEntity<Object> addOwnerVehicleDriverDetails(@PathVariable("quarryId") Long quarryId, @RequestBody AddTruckReq addTruckReq) throws AuthenticationException {
        return truckService.addOwnerVehicleDriverDetails(quarryId, addTruckReq);
    }

    @GetMapping("/vehicle/{truckId}/{quarryId}")
    public ResponseEntity<TruckInfoResDTO> fetchTruckById(@PathVariable("truckId") Long truckId, @PathVariable("quarryId") Long quarryId) {
        return truckService.fetchTruckById(truckId, quarryId);
    }

    @GetMapping("/vehicleSearch/{truckNo}/{quarryId}")
    public ResponseEntity<List<SearchTruckResDTOList>> fetchTruckByNo(@PathVariable("truckNo") String truckNo, @PathVariable("quarryId") Long quarryId) {
        return truckService.fetchTruckByNo(truckNo, quarryId);
    }

    @PostMapping(value = "/generate-truck-entry-pass")
    public ResponseEntity<LoadDetail> generateTruckEntryPass(@RequestBody LoadReqDTO loadReqDTO) throws AuthenticationException {
        return truckService.generateTruckEntryPass(loadReqDTO);
    }

    @GetMapping("/search-entered-vehicle/{truckNo}")
    public ResponseEntity<List<LoadDetail>> fetchEnteredTruckByNo(@PathVariable("truckNo") String truckNo) {
        return truckService.fetchEnteredTruckByNo(truckNo);
    }

    @GetMapping("/loadDetails/{loadId}")
    public LoadDetail fetchLoadDetailsById(@PathVariable("loadId") Long loadId) {
        return truckService.fetchLoadDetailsById(loadId);
    }

    @GetMapping("/truck-exit/{loadId}")
    public LoadDetail updateTruckExitStatus(@PathVariable("loadId") Long loadId) {
        return truckService.updateTruckExitStatus(loadId);
    }

    @PutMapping("/updateLoadDetail/{loadId}")
    public LoadDetail updateLoadDetail(@PathVariable("loadId") Long loadId, @RequestBody LoadReqDTO loadReqDTO) {
        return truckService.updateLoadDetail(loadId, loadReqDTO);
    }

    @GetMapping("/admin/getAllOwner/{quarryId}")
    public ResponseEntity<List<TruckInfoResDTO>> getAllOwner(@PathVariable("quarryId") Long quarryId) {
        return truckService.getAllOwner(quarryId);
    }

    @PostMapping(value = "/admin/addVehicleOwner")
    public ResponseEntity<Object> addVehicleOwner(@RequestBody AddTruckReq addTruckReq) throws AuthenticationException {
        return truckService.addVehicleOwner(addTruckReq);
    }

}
