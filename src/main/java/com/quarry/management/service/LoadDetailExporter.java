package com.quarry.management.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.quarry.management.DTO.LoadResponseDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadDetailExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<LoadResponseDTO> listUsers;

    public LoadDetailExporter(List<LoadResponseDTO> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Load Detail");

        Row row = sheet.createRow(0);
        sheet.setColumnWidth(3, 25 * 256);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Owner Name", style);
        createCell(row, 1, "Owner Mobile", style);
        createCell(row, 2, "Truck No", style);
        createCell(row, 3, "Truck Capacity", style);
        createCell(row, 4, "Truck in Time", style);
        createCell(row, 5, "Truck out Time", style);
        createCell(row, 6, "Product Name", style);
        createCell(row, 7, "Product Unit Cost", style);
        createCell(row, 8, "Loaded Unit", style);
        createCell(row, 9, "Loaded Amount", style);
        createCell(row, 10, "Challan Unit Cost", style);
        createCell(row, 11, "Total Challan", style);
        createCell(row, 12, "Total Challan Amount", style);
        createCell(row, 13, "Payment Type", style);
        createCell(row, 14, "Payment Status", style);
        createCell(row, 15, "Total Amount", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (LoadResponseDTO loadDetail : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, loadDetail.getTruckOwnerName(), style);
            createCell(row, columnCount++, loadDetail.getTruckOwnerMobileNo(), style);
            createCell(row, columnCount++, loadDetail.getTruckVehicleNo(), style);
            createCell(row, columnCount++, loadDetail.getTruckCapacity(), style);
            createCell(row, columnCount++, loadDetail.getTruckInTime() == null ? "" : loadDetail.getTruckInTime().toString() , style);
            createCell(row, columnCount++, loadDetail.getTruckOutTime() == null ? "" : loadDetail.getTruckOutTime().toString() , style);
            createCell(row, columnCount++, loadDetail.getProductName(), style);
            createCell(row, columnCount++, loadDetail.getProductUnitCost().toString(), style);
            createCell(row, columnCount++, loadDetail.getLoadUnit().toString(), style);
            createCell(row, columnCount++, loadDetail.getLoadAmt().toString(), style);
            createCell(row, columnCount++, loadDetail.getChallanUnitCost().toString(), style);
            createCell(row, columnCount++, loadDetail.getTotalChallan() == null ? "" : loadDetail.getTotalChallan().toString(), style);
            createCell(row, columnCount++, loadDetail.getTotalChallanAmt() == null ? "" : loadDetail.getTotalChallanAmt().toString(), style);
            createCell(row, columnCount++, loadDetail.getPaymentType() == true ? "Cash" : "Credit", style);
            createCell(row, columnCount++, loadDetail.getPaymentStatus() == true ? "Success" : "Pending", style);
            createCell(row, columnCount++, loadDetail.getTotalAmt().toString(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}