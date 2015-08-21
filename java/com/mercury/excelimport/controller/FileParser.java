package com.mercury.excelimport.controller;

import com.mercury.excelimport.model.File;
import com.mercury.excelimport.model.FileRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Daniel on 2015-08-21.
 */
public class FileParser
{
    public File parse(InputStream stream)
    {
        //Get the workbook instance for XLS file
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(stream);
        } catch (IOException e) {
            return null;
        }

        //Get first sheet from the workbook
        assert workbook != null;
        XSSFSheet sheet = workbook.getSheetAt(0);

        File file = new File();

        //Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = sheet.iterator();
        while(rowIterator.hasNext())
        {
            org.apache.poi.ss.usermodel.Row row = rowIterator.next();

            if (row.getRowNum() == 0)
                continue;

            String system = row.getCell(0).toString();
            String request = row.getCell(1).toString();
            String orderNumber = row.getCell(2).toString();

            java.util.Date fromDate;
            if (Utilities.tryParseCellDate(row.getCell(3)))
                fromDate = row.getCell(3).getDateCellValue();
            else {
                System.out.println("Parse error: fromDate is of type: " + row.getCell(3).getCellType());
                return null;
            }

            java.util.Date toDate;
            if (Utilities.tryParseCellDate(row.getCell(4)))
                toDate = row.getCell(4).getDateCellValue();
            else {
                System.out.println("Parse error: toDate is of type: " + row.getCell(4).getCellType());
                return null;
            }

            float amount;
            if (Utilities.tryParseFloat(row.getCell(5).toString()))
                amount = Float.parseFloat(row.getCell(5).toString());
            else {
                System.out.println("Parse error: amount is of type: " + row.getCell(5).getCellType());
                return null;
            }

            String amountType = row.getCell(6).toString();
            String amountPeriod = row.getCell(7).toString();

            float authPercent;
            if (Utilities.tryParseFloat(row.getCell(8).toString()))
                authPercent = Float.parseFloat(row.getCell(8).toString());
            else {
                System.out.println("Parse error: authPercent is of type: " + row.getCell(8).getCellType());
                return null;
            }

            String active = row.getCell(9).toString();

            file.addRow(new FileRow(system, request, orderNumber, fromDate, toDate,
                    amount, amountType, amountPeriod, authPercent, Boolean.parseBoolean(active)));

        }

        return file;
    }
}
