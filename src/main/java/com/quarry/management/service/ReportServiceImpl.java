package com.quarry.management.service;

import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.DTO.ReportReqDTO;
import com.quarry.management.config.TwilioConfig;
import com.quarry.management.utils.Constants;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private JdbcOperations dataSource;

    @Autowired
    private TwilioConfig twilioConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public List<LoadResponseDTO> generateReport(Long quarryId, ReportReqDTO reportReqDTO) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        String sql = "SELECT ld.product_unit_cost, " +
                "ld.load_unit, " +
                "ld.load_amt, " +
                "ld.challan_unit_cost, " +
                "ld.total_challan, " +
                "ld.total_challan_amt, " +
                "ld.vehicle_in_time as truckInTime, " +
                "ld.vehicle_out_time as truckOutTime, " +
                "ld.load_status, " +
                "ld.entry_status, " +
                "ld.total_amt, " +
                "ld.payment_status, " +
                "ld.payment_type, " +
                "ld.loaded_at as loadDate, " +
                "ld.load_id, " +
                "ad.phone as adminPhone, " +
                "tod.owner_id as truckOwnerId, " +
                "tod.truck_owner_name as truckOwnerName, " +
                "tod.truck_owner_mobile_no, " +
                "tod.truck_owner_address, " +
                "td.truck_no as truckVehicleNo, " +
                "td.truck_capacity as truckCapacity, " +
                "tdd.driver_name as truckDriverName, " +
                "tdd.driver_mobile_no as truckDriveMobileNo, " +
                "p.product_name " +
                " FROM load_details ld " +
                "JOIN truck_owner_details tod ON ld.owner_id = tod.owner_id " +
                "JOIN truck_details td ON ld.truck_id = td.truck_id " +
                "JOIN truck_driver_details tdd ON ld.driver_id = tdd.driver_id " +
                "JOIN products p ON ld.product_id = p.product_id " +
                "JOIN quarry_details qd ON ld.quarry_id = qd.quarry_id " +
                "JOIN admin ad ON qd.admin_id = ad.admin_id " +
                "WHERE ld.load_id > 0 " +
                "AND ld.quarry_id ="+reportReqDTO.getQuarryId() +
                " AND (str_to_date(ld.loaded_at,'%Y-%m-%d')  BETWEEN '"+reportReqDTO.getStartDate()+"' AND '"+reportReqDTO.getEndDate()+"') ";
        if(reportReqDTO.getProductId() != null) {
            sql += " AND ld.product_id=:product_id";
        }
        if(reportReqDTO.getOwnerId() != null) {
            sql += " AND ld.owner_id=:owner_id";
        }
        if(reportReqDTO.getPaymentType() != null) {
            sql += " AND ld.payment_type=:payment_type ";
        }
        if(reportReqDTO.getPaymentStatus() != null) {
            sql += " AND ld.payment_status =:payment_status ";
        }
        if(reportReqDTO.getTruckId() != null) {
            sql += " AND ld.truck_id=:truck_id ";
        }
        SqlParameterSource param = new MapSqlParameterSource("quarry_id", reportReqDTO.getQuarryId())
                .addValue("product_id",  reportReqDTO.getProductId())
                .addValue("owner_id", reportReqDTO.getOwnerId())
                .addValue("payment_type", reportReqDTO.getPaymentType())
                .addValue("payment_status", reportReqDTO.getPaymentStatus());
        List<LoadResponseDTO> result = template.query(sql, param, BeanPropertyRowMapper.newInstance(LoadResponseDTO.class));
        NotificationForAdmin(result, reportReqDTO);
        NotificationForOwner(result, reportReqDTO);
        return result;
    }

    @Override
    public List<LoadResponseDTO> sendNotification(ReportReqDTO reportReqDTO) {
        return generateReport(null,reportReqDTO);
    }

    private void NotificationForOwner(List<LoadResponseDTO> loadResponseDTOList, ReportReqDTO reportReqDTO){
        SimpleDateFormat sdf = new SimpleDateFormat();
        Map<Long,List<LoadResponseDTO>> ownerMap = loadResponseDTOList.stream().collect(Collectors.groupingBy(LoadResponseDTO :: getTruckOwnerId));
        System.out.println(ownerMap);
        try {
            for (List<LoadResponseDTO> loadResponseDTOS : ownerMap.values()){
                StringBuilder s = new StringBuilder();
                s.append("\n\n"+"Weekly Report Details :- "+"\n\n");
                s.append("From : "+reportReqDTO.getStartDate() + " - To : "+reportReqDTO.getEndDate() +"\n\n");
                PhoneNumber to = new PhoneNumber("+91 "+loadResponseDTOS.get(0).getTruckOwnerMobileNo());
                PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
                loadResponseDTOS.forEach(details ->{
                        s.append("Driver name        : "+ details.getTruckDriverName()+"\n");
                        s.append("Product             : " + details.getProductName()+"\n");
                        s.append("Load unit          : " + details.getLoadUnit()+"\n");
                        s.append("Load amount      : " + details.getTotalAmt()+"\n");
                        if(details.getPaymentType() == true) {
                            s.append("Payment Type   : " +"Cash"+"\n");
                        } else {
                            s.append("Payment Type   : " +"Credit"+"\n");
                        }
                        if(details.getPaymentStatus() == true) {
                            s.append("Payment status  : " +"Success"+"\n");
                        } else {
                            s.append("Payment status  : " +"Pending"+"\n");
                        }
                        String date = sdf.format(details.getLoadDate());
                        s.append("Loaded Date    : " + date+"\n\n");
                });
                Message message = Message.creator(to, from, s.toString()).create();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    private void NotificationForAdmin(List<LoadResponseDTO> loadResponseDTOList, ReportReqDTO reportReqDTO){
        SimpleDateFormat sdf = new SimpleDateFormat();
        StringBuilder s = new StringBuilder();
          s.append("\n\n"+"Weekly Report Details :- "+"\n\n");
          s.append("From : "+reportReqDTO.getStartDate() + " - To : "+reportReqDTO.getEndDate() +"\n\n");
        try {
            PhoneNumber to = new PhoneNumber("+91 "+loadResponseDTOList.get(0).getAdminPhone());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            loadResponseDTOList.forEach(details -> {
                s.append("Owner name        : "+ details.getTruckOwnerName()+"\n");
                s.append("Product             : " + details.getProductName()+"\n");
                s.append("Load unit          : " + details.getLoadUnit()+"\n");
                s.append("Load amount      : " + details.getTotalAmt()+"\n");
                if(details.getPaymentType() == true) {
                    s.append("Payment Type   : " +"Cash"+"\n");
                } else {
                    s.append("Payment Type   : " +"Credit"+"\n");
                }
                if(details.getPaymentStatus() == true) {
                    s.append("Payment status  : " +"Success"+"\n");
                } else {
                    s.append("Payment status  : " +"Pending"+"\n");
                }
                String date = sdf.format(details.getLoadDate());
                s.append("Loaded Date    : " + date+"\n\n");
            });
            Message message = Message.creator(to, from, s.toString()).create();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void generateExcel(Long quarryId, ReportReqDTO reportReqDTO, HttpServletResponse response) throws IOException {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        String sql = "SELECT ld.product_unit_cost, " +
                "ld.load_unit, " +
                "ld.load_amt, " +
                "ld.challan_unit_cost, " +
                "ld.total_challan, " +
                "ld.total_challan_amt, " +
                "ld.vehicle_in_time as truckInTime, " +
                "ld.vehicle_out_time as truckOutTime, " +
                "ld.load_status, " +
                "ld.entry_status, " +
                "ld.total_amt, " +
                "ld.payment_status, " +
                "ld.payment_type, " +
                "ld.loaded_at as loadDate, " +
                "ld.load_id, " +
                "tod.truck_owner_name as truckOwnerName, " +
                "tod.truck_owner_mobile_no, " +
                "tod.truck_owner_address, " +
                "td.truck_no as truckVehicleNo, " +
                "td.truck_capacity as truckCapacity, " +
                "tdd.driver_name as truckDriverName, " +
                "tdd.driver_mobile_no as truckDriveMobileNo, " +
                "p.product_name " +
                " FROM load_details ld " +
                "JOIN truck_owner_details tod ON ld.owner_id = tod.owner_id " +
                "JOIN truck_details td ON ld.truck_id = td.truck_id " +
                "JOIN truck_driver_details tdd ON ld.driver_id = tdd.driver_id " +
                "JOIN products p ON ld.product_id = p.product_id " +
                "WHERE ld.load_id > 0 " +
                "AND ld.quarry_id ="+reportReqDTO.getQuarryId() +
                " AND (str_to_date(ld.loaded_at,'%Y-%m-%d')  BETWEEN '"+reportReqDTO.getStartDate()+"' AND '"+reportReqDTO.getEndDate()+"') ";
        if(reportReqDTO.getProductId() != null) {
            sql += " AND ld.product_id=:product_id";
        }
        if(reportReqDTO.getOwnerId() != null) {
            sql += " AND ld.owner_id=:owner_id";
        }
        if(reportReqDTO.getPaymentType() != null) {
            sql += " AND ld.payment_type=:payment_type ";
        }
        if(reportReqDTO.getPaymentStatus() != null) {
            sql += " AND ld.payment_status =:payment_status ";
        }
        if(reportReqDTO.getTruckId() != null) {
            sql += " AND ld.truck_id=:truck_id ";
        }
        SqlParameterSource param = new MapSqlParameterSource("quarry_id", reportReqDTO.getQuarryId())
                .addValue("product_id",  reportReqDTO.getProductId())
                .addValue("owner_id", reportReqDTO.getOwnerId())
                .addValue("payment_type", reportReqDTO.getPaymentType())
                .addValue("payment_status", reportReqDTO.getPaymentStatus());
        List<LoadResponseDTO> result = template.query(sql, param, BeanPropertyRowMapper.newInstance(LoadResponseDTO.class));
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        LoadDetailExporter excelExporter = new LoadDetailExporter(result);

        excelExporter.export(response);
    }
}
