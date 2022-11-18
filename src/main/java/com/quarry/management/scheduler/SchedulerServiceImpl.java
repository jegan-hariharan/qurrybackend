package com.quarry.management.scheduler;

import com.quarry.management.DTO.ReportReqDTO;
import com.quarry.management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;


@Component
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private ReportService reportService;

    @Scheduled(cron = "0 0 6 * * SUN")
    @Override
    public void generateReport() {
        ReportReqDTO reportReqDTO = new ReportReqDTO();
        LocalDate localDate = LocalDate.now().minusDays(7);
        reportReqDTO.setStartDate(Date.valueOf(localDate));
        reportReqDTO.setEndDate(Date.valueOf(LocalDate.now()));
        reportReqDTO.setQuarryId(1L);
        reportService.sendNotification(reportReqDTO);
    }
}
