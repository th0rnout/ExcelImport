package com.mercury.excelimport.controller;

import com.mercury.excelimport.model.File;
import com.mercury.excelimport.model.FileRow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Daniel on 2015-08-21.
 */
@Component
public class FileParser
{
    private ArrayList<String> errors = new ArrayList<String>();

    public File parse(InputStream stream)
    {
        errors.clear();

        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        if(wb == null)
            return null;

        Sheet sheet = wb.getSheetAt(0);

        File file = new File();

        //Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = sheet.iterator();
        while(rowIterator.hasNext())
        {
            org.apache.poi.ss.usermodel.Row row = rowIterator.next();

            if (row.getRowNum() == 0)
            {
                boolean rightFile = true;

                if (row.getCell(0) == null || !row.getCell(0).toString().equals("system"))
                {
                    System.out.println("wrong 0");
                    rightFile = false;
                }
                if (row.getCell(1) == null || !row.getCell(1).toString().equals("request"))
                {
                    System.out.println("wrong 1");
                    rightFile = false;
                }
                if (row.getCell(2) == null || !row.getCell(2).toString().equals("order_number")) {
                    System.out.println("wrong 2");
                    rightFile = false;
                }
                if (row.getCell(3) == null || !row.getCell(3).toString().equals("from_date"))
                {
                    System.out.println("wrong 3");
                    rightFile = false;
                }
                if (row.getCell(4) == null || !row.getCell(4).toString().equals("to_date"))
                {
                    System.out.println("wrong 4");
                    rightFile = false;
                }
                if (row.getCell(5) == null || !row.getCell(5).toString().equals("amount"))
                {
                    System.out.println("wrong 5");
                    rightFile = false;
                }
                if (row.getCell(6) == null || !row.getCell(6).toString().equals("amount_type"))
                {
                    System.out.println("wrong 6");
                    rightFile = false;
                }
                if (row.getCell(7) == null || !row.getCell(7).toString().equals("amount_period"))
                {
                    System.out.println("wrong 7");
                    rightFile = false;
                }
                if (row.getCell(8) == null || !row.getCell(8).toString().equals("authorization_percent"))
                {
                    System.out.println("wrong 8");
                    rightFile = false;
                }
                if (row.getCell(9) == null || !row.getCell(9).toString().equals("active"))
                {
                    System.out.println("wrong 9");
                    rightFile = false;
                }

                if (!rightFile)
                {
                    errors.add("wrong file structure.");
                    return null;
                }

                continue;
            }


            String system = row.getCell(0).toString();
            if (system.length() > 50)
                errors.add("system is too long. Maximum length is 50 characters.");

            String request = row.getCell(1).toString();
            if (request.length() > 12)
                errors.add("request is too long. Maximum length is 12 characters.");

            String orderNumber = row.getCell(2).toString();
            if (orderNumber.length() > 12)
                errors.add("order_number is too long. Maximum length is 12 characters.");

            java.util.Date fromDate = null;
            if (Utilities.tryParseCellDate(row.getCell(3)))
                fromDate = row.getCell(3).getDateCellValue();
            else {
                System.out.println("Parse error: fromDate is of type: " + row.getCell(3).getCellType());
                errors.add("wrong from_date type.");
            }

            java.util.Date toDate = null;
            if (Utilities.tryParseCellDate(row.getCell(4)))
                toDate = row.getCell(4).getDateCellValue();
            else {
                System.out.println("Parse error: toDate is of type: " + row.getCell(4).getCellType());
                errors.add("wrong to_date type.");
            }

            float amount = 0;
            if (Utilities.tryParseFloat(row.getCell(5).toString()))
                amount = Float.parseFloat(row.getCell(5).toString());
            else {
                System.out.println("Parse error: amount is of type: " + row.getCell(5).getCellType());
                errors.add("wrong amount type.");
            }

            String amountType = row.getCell(6).toString();
            if (amountType.length() > 5)
                errors.add("amount_type is too long. Maximum length is 5 characters.");

            String amountPeriod = row.getCell(7).toString();
            if (amountPeriod.length() > 5)
                errors.add("amount_period is too long. Maximum length is 5 characters.");

            float authPercent = 0;
            if (Utilities.tryParseFloat(row.getCell(8).toString()))
                authPercent = Float.parseFloat(row.getCell(8).toString());
            else
            {
                System.out.println("Parse error: authPercent is of type: " + row.getCell(8).getCellType());
                errors.add("wrong authorization_percent type.");
            }

            Boolean active = false;
            if (Utilities.tryParseBoolean(row.getCell(9).toString()))
                active = Utilities.tryParseBoolean(row.getCell(9).toString());
            else
            {
                System.out.println("Parse error: active is of type: " + row.getCell(9).getCellType());
                errors.add("wrong active type.");
            }

            file.addRow(new FileRow(system, request, orderNumber, fromDate, toDate,
                    amount, amountType, amountPeriod, authPercent, active));
        }

        if (errors.size() > 0) {
            System.out.print("File not uploaded");

            for (int i = 0; i < errors.size(); ++i)
                System.out.println(errors.get(i));

            return null;
        }
        else {
            System.out.print("File uploaded");
            return file;
        }
    }

    public ArrayList<String> getErrors()
    {
        return errors;
    }
}
