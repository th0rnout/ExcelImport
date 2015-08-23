package com.mercury.excelimport.controller;

import com.mercury.excelimport.DBConnector;
import com.mercury.excelimport.model.FileRow;
import com.mercury.excelimport.model.SystemContract;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class FileController
{
    private DBConnector db;
    private FileParser parser;

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public FileController(DBConnector db, FileParser parser)
    {
        this.db = db;
        this.parser = parser;
    }

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
            // TODO: Tell user what went wrong while parsing
            /////////
            if (parsedFile != null) {
                if(db.validateFile(parsedFile))
                {
                    db.handleFile(parsedFile);
                    model.addObject("success", "Upload successful!");
                }
                else {
                    System.out.println("Incorrect structure: System not found.");
                    model.addObject("error", "Invalid structure: System not found");
                }
            }
            else {
                System.out.println("File could not be parsed.");
                model.addObject("error", "File could not be parsed.");
            }
        }
        else
        {
            System.out.println("Wrong content type: " + file.getContentType());
            model.addObject("error", "Wrong file type. Only files of type XLS and XLSX are allowed.");
        }

        ArrayList<SystemContract> list = db.getContracts();
        if (list != null)
        {
            ArrayList<FileRow> rows = convertContracts(list);
            model.addObject("rows", rows);
        }

        model.addObject("row", new FileRow());

        return model;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        ArrayList<SystemContract> list = db.getContracts();
        if (list != null)
        {
            ArrayList<FileRow> rows = convertContracts(list);
            model.addAttribute("rows", rows);
        }

        model.addAttribute("row", new FileRow());

        return "index.jsp";
    }

    @RequestMapping(value = "/deleteRow", method = RequestMethod.POST)
    public String deleteRow(@RequestParam("id") int id)
    {
        System.out.println(id);

        this.db.deleteRow(id);

        return "ajax.jsp";
    }


    @RequestMapping(value = "/saveOrUpdateRow")
    public ModelAndView saveOrUpdateRow(@Valid @ModelAttribute("row") FileRow row,
                         BindingResult errors, HttpServletRequest request)
    {
        ModelAndView model = new ModelAndView("ajax.jsp");

        System.out.println(row.getToDate());

        if(!errors.hasErrors()) {
            if (this.db.validateRow(row))
            {
                this.db.handleRow(row);
                model.addObject("result", "success");
            }
            else
                model.addObject("result", "system-error");

        }
        else
            model.addObject("result", "error");

        return model;
    }

    public ArrayList<FileRow> convertContracts(ArrayList<SystemContract> list)
    {
        ArrayList<FileRow> rows = new ArrayList<FileRow>();

        for (Iterator<SystemContract> it = list.iterator(); it.hasNext(); ) {
            SystemContract contract = it.next();

            rows.add(new FileRow(db.getSystem(contract.getSystemId()).getName(), contract.getRequest(), contract.getOrderNumber(), contract.getFromDate(),
                    contract.getToDate(), contract.getAmount(), contract.getAmountType(), contract.getAmountPeriod(),
                    contract.getAuthPercent(), contract.isActive(), contract.getId()));
        }

        return rows;
    }
}