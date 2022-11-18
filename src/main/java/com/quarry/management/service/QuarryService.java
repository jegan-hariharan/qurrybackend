package com.quarry.management.service;

import com.quarry.management.DTO.QuarryReqInfo;
import com.quarry.management.entity.Employee;
import com.quarry.management.entity.Manager;
import com.quarry.management.entity.Quarry;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuarryService {
    public Quarry saveQuarryDetails(QuarryReqInfo quarryReqInfo);

    public List<Quarry> fetchAllQuarry(String role, Long id);

    public Quarry fetchQuarryById(Long quarryId);

    public Quarry updateQuarryDetail(Long quarryId, Quarry quarry);

    Manager getQuarryByManager(String role, Long id);

    Employee getQuarryByEmployee(String role, Long id);
}
