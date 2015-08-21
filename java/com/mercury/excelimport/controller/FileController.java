package com.mercury.excelimport.controller;

import com.mercury.excelimport.DBConnector;
import javafx.scene.control.Cell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class FileController
{
    private DBConnector db = new DBConnector();

    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping(method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("excel") MultipartFile file) {

        if (!file.isEmpty()) {
            InputStream stream = null;
            try {
                stream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext())
            {
                org.apache.poi.ss.usermodel.Row row = rowIterator.next();

                Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext())
                {
                    org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();

                    System.out.println(cell.toString());
                }
            }
            // store the bytes somewhere
            return "hello";
        }
        
        return "hello";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        //db.handleRow(null);

        return "hello";
    }

}