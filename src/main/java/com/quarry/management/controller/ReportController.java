package com.quarry.management.controller;

import com.quarry.management.DTO.LoadReqDTO;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.DTO.ReportReqDTO;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.service.LoadDetailService;
import com.quarry.management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/admin/generateReport/{quarryId}")
    public List<LoadResponseDTO> generateReport(@PathVariable("quarryId") Long quarryId, @RequestBody ReportReqDTO reportReqDTO) {
        return reportService.generateReport(quarryId, reportReqDTO);
    }

    @PostMapping("/admin/generateExcel/{quarryId}")
    public void generateExcel(@PathVariable("quarryId") Long quarryId, @RequestBody ReportReqDTO reportReqDTO, HttpServletResponse response) throws IOException {
        reportService.generateExcel(quarryId, reportReqDTO, response);
    }
}
