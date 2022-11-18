package com.quarry.management.controller;

import com.quarry.management.DTO.QuarryReqInfo;
import com.quarry.management.entity.Employee;
import com.quarry.management.entity.Manager;
import com.quarry.management.entity.Quarry;
import com.quarry.management.service.QuarryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuarryController {

    @Autowired
    private QuarryService quarryService;

    @PostMapping("/admin/quarryinfo")
    public Quarry saveQuarryDetails(@RequestBody QuarryReqInfo quarryReqInfo) {
        return quarryService.saveQuarryDetails(quarryReqInfo);
    }

    @GetMapping("/allQuarry/{role}/{id}")
    public List<Quarry> fetchAllQuarry(@PathVariable("role") String role, @PathVariable("id") Long id) {
        return quarryService.fetchAllQuarry(role,id);
    }

    @GetMapping("/allQuarry/{id}")
    public Quarry fetchQuarryById(@PathVariable("id") Long quarryId) {
        return quarryService.fetchQuarryById(quarryId);
    }

    @PutMapping("/allQuarry/{id}")
    public Quarry updateQuarryDetail(@PathVariable("id") Long quarryId, @RequestBody Quarry quarry) {
        return quarryService.updateQuarryDetail(quarryId, quarry);
    }

    @GetMapping("/getQuarryByManager/{role}/{id}")
    public Manager getQuarryByManager(@PathVariable("role") String role, @PathVariable("id") Long id) {
        return quarryService.getQuarryByManager(role, id);
    }

    @GetMapping("/getQuarryByEmployee/{role}/{id}")
    public Employee getQuarryByEmployee(@PathVariable("role") String role, @PathVariable("id") Long id) {
        return quarryService.getQuarryByEmployee(role, id);
    }

}
