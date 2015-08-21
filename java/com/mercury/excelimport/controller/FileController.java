package com.mercury.excelimport.controller;

import com.mercury.excelimport.DBConnector;
import com.mercury.excelimport.model.FileRow;
import com.mercury.excelimport.model.SystemContract;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class FileController
{
    private DBConnector db = new DBConnector();
    private FileParser parser = new FileParser();

    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView handleFormUpload(@RequestParam("excel") MultipartFile file) {

        ModelAndView model = new ModelAndView("index.jsp");

        if (!file.isEmpty()
                && (file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    || file.getContentType().equalsIgnoreCase("application/vnd.ms-excel")))
        {
            System.out.println("Content type: " + file.getContentType());

            InputStream stream = null;
            try {
                stream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            com.mercury.excelimport.model.File parsedFile = parser.parse(stream);

            /////////
            // TODO: Support .xls files (HSSFWorkbook)
            /////////
            if (parsedFile != null) {
                db.handleFile(parsedFile);
            }
            else {
                System.out.println("File could not be parsed.");
            }
        }
        else
        {
            System.out.println("Wrong content type: " + file.getContentType());
        }

        ArrayList<SystemContract> list = db.getContracts();
        if (list != null)
            model.addObject("contracts", list);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        //db.handleRow(null);

        return "index.jsp";
    }

}