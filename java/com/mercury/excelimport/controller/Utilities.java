package com.mercury.excelimport.controller;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by Daniel on 2015-08-22.
 */
public class Utilities
{
    public static boolean tryParseCellDate(Cell cell)
    {
        try {
            cell.getDateCellValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean tryParseFloat(String value)
    {
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean tryParseBoolean(String value)
    {
        try {
            Boolean.parseBoolean(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
