package com.mercury.excelimport.controller;

import com.mercury.excelimport.DBConnector;
import com.mercury.excelimport.model.FileRow;
import com.mercury.excelimport.model.SystemContract;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class FileController implements HandlerExceptionResolver
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
    public ModelAndView handleFormUpload(@RequestParam("excel") MultipartFile file) throws SizeLimitExceededException
    {
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


            if (parsedFile != null) {
                if(db.validateFile(parsedFile))
                {
                    db.handleFile(parsedFile);
                    model.addObject("success", "Upload successful!");
                }
                else {
                    System.out.println("Incorrect structure: System not found.");
                    ArrayList<String> errors = new ArrayList<String>();
                    errors.add("Invalid structure: System not found");
                    model.addObject("errors", errors);
                }
            }
            else {
                System.out.println("File could not be parsed.");
                model.addObject("errors", parser.getErrors());
            }
        }
        else
        {
            System.out.println("Wrong content type: " + file.getContentType());
            ArrayList<String> errors = new ArrayList<String>();
            errors.add("Wrong file type. Only files of type XLS and XLSX are allowed.");
            model.addObject("errors", errors);
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

        System.out.println(row.getContractId());

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

    /*** Trap Exceptions during the upload and show errors back in view form ***/
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception exception)
    {

        ModelAndView model = new ModelAndView("index.jsp");

        if (exception instanceof MaxUploadSizeExceededException ||
                exception instanceof MultipartException)
        {
            if (exception instanceof MaxUploadSizeExceededException ||
                    ExceptionUtils.getRootCause(exception).toString().contains("SizeLimitExceededException"))
            {
                ArrayList<String> errors = new ArrayList<String>();
                errors.add("Uploaded file is too big. Maximum file size is 100 kB.");
                model.addObject("errors", errors);
            }
            else
            {
                ArrayList<String> errors = new ArrayList<String>();
                errors.add("Unexpected error: " + exception.getMessage());
                model.addObject("errors", errors);
            }
        }
        else
        {
            ArrayList<String> errors = new ArrayList<String>();
            errors.add("Unexpected error: " + exception.getMessage());
            model.addObject("errors", errors);
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