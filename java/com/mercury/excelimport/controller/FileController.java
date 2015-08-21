package com.mercury.excelimport.controller;

import com.mercury.excelimport.DBConnector;
import com.mercury.excelimport.model.FileRow;
import com.mercury.excelimport.model.SystemContract;
import javafx.scene.control.Cell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class FileController
{
    private DBConnector db = new DBConnector();

    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView handleFormUpload(@RequestParam("excel") MultipartFile file) {

        logger.info("Rekt");

        ModelAndView model = new ModelAndView("/hello.jsp");

        db.handleRow(new FileRow("B020", "2222", "22/2011", new Date(), new Date(), 100.00f, "NET", "MONTH", 2, true));

        ArrayList<SystemContract> list = db.getContracts();

        if(list != null)
            model.addObject("contracts", list);

        if (!file.isEmpty())
        {
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
        }
        
        return model;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        //db.handleRow(null);

        return "hello.jsp";
    }

}