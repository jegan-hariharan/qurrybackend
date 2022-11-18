package com.quarry.management.service;

import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.DTO.ReportReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ReportService {
    List<LoadResponseDTO> generateReport(Long quarryId, ReportReqDTO reportReqDTO);

    List<LoadResponseDTO> sendNotification(ReportReqDTO reportReqDTO);


    void generateExcel(Long quarryId, ReportReqDTO reportReqDTO, HttpServletResponse response) throws IOException;
}
