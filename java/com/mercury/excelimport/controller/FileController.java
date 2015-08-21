package com.mercury.excelimport.controller;

import com.mercury.excelimport.DBConnector;
import com.mercury.excelimport.model.FileRow;
import javafx.scene.control.Cell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Date;
import java.util.Iterator;

public class FileController implements Controller
{
    private DBConnector db = new DBConnector();

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Rekt");

        ModelAndView model = new ModelAndView("/WEB-INF/pages/hello.jsp");

        String message;

        db.handleRow(new FileRow("B020", "2222", "22/2011", new Date(), new Date(), 100.00f, "NET", "MONTH", 2, true));

        if (request.getMethod() == "GET")
        {
            message = "No file";
        }
        else
        {
            message = "File";


            Part p1 = request.getPart("excel");

            //String file = (request.getParameter("excel")).getBytes().toString();
            //logger.info(file);

            InputStream file = p1.getInputStream();

            //Get the workbook instance for XLS file
            XSSFWorkbook workbook = null;
            try {
                workbook = new XSSFWorkbook(file);
            } catch (IOException e) {
                logger.error("SHIET, DIS EXEL GOT MAD");
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

        model.addObject("type", message);

        return model;
    }
}