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

        ModelAndView model = new ModelAndView("/hello.jsp");

        if (!file.isEmpty()) {

            logger.info("Content type: " + file.getContentType());

            InputStream stream = null;
            try {
                stream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (parser.parse(stream)) {

                Iterator<FileRow> iter = parser.getFile().getRowsIterator();
                while (iter.hasNext())
                {
                    FileRow row = iter.next();
                }

                if(db.validateFile(parser.getFile()))
                    db.handleFile(parser.getFile());
                else;
                    // TODO: file structure error
            }
        }

        ArrayList<SystemContract> list = db.getContracts();
        if (list != null)
            model.addObject("contracts", list);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        ArrayList<SystemContract> list = db.getContracts();
        if (list != null)
            model.addAttribute("contracts", list);

        return "hello.jsp";
    }

    @RequestMapping(value = "/deleteRow", method = RequestMethod.POST)
    public String deleteRow(@RequestParam("id") int id)
    {
        System.out.println(id);

        this.db.deleteRow(id);

        return "debug.jsp";
    }

}