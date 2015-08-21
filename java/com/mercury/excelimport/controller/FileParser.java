package com.mercury.excelimport.controller;

import com.mercury.excelimport.model.File;
import com.mercury.excelimport.model.FileRow;
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
    private File file;

    public FileParser()
    {
        file = new File();
    }


    public boolean parse(InputStream stream)
    {
        file.clearRows();

        //Get the workbook instance for XLS file
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get first sheet from the workbook
        assert workbook != null;
        XSSFSheet sheet = workbook.getSheetAt(0);

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
            java.util.Date fromDate = row.getCell(3).getDateCellValue();
            java.util.Date toDate = row.getCell(4).getDateCellValue();
            String amount = row.getCell(5).toString();
            String amountType = row.getCell(6).toString();
            String amountPeriod = row.getCell(7).toString();
            String authPercent = row.getCell(8).toString();
            String active = row.getCell(9).toString();

            file.addRow(new FileRow(system, request, orderNumber, fromDate, toDate,
                    Float.parseFloat(amount), amountType, amountPeriod, Float.parseFloat(authPercent), Boolean.parseBoolean(active)));

            /*
            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
            while(cellIterator.hasNext())
            {
                org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
            }
            */
        }

        return true;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
