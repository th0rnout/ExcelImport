package com.mercury.excelimport.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fedake on 2015-08-20.
 */
public class FileRow
{
    private String system;
    private String request;
    private String orderNumber;
    private Date fromDate;
    private Date toDate;
    private float amount;
    private String amountType;
    private String amountPeriod;
    private float authPercent;
    private boolean active;
    private int contractId;

    public FileRow()
    {

    }

    public FileRow(String system, String request, String orderNumber, Date fromDate, Date toDate, float amount, String amountType, String amountPeriod, float authPercent, boolean active)
    {
        this.system = system;
        this.request = request;
        this.orderNumber = orderNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.amount = amount;
        this.amountType = amountType;
        this.amountPeriod = amountPeriod;
        this.authPercent = authPercent;
        this.active = active;
        this.contractId = 0;

    }

    public FileRow(String system, String request, String orderNumber, Date fromDate, Date toDate, float amount, String amountType, String amountPeriod, float authPercent, boolean active, int contractId)
    {
        this.system = system;
        this.request = request;
        this.orderNumber = orderNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.amount = amount;
        this.amountType = amountType;
        this.amountPeriod = amountPeriod;
        this.authPercent = authPercent;
        this.active = active;
        this.contractId = contractId;

    }


    // Getters and Setters

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        try {
            this.fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getToDate() {
        return toDate;
    }


    public void setToDate(String toDate) {
        try {
            this.toDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAmountPeriod() {
        return amountPeriod;
    }

    public void setAmountPeriod(String amountPeriod) {
        this.amountPeriod = amountPeriod;
    }

    public float getAuthPercent() {
        return authPercent;
    }

    public void setAuthPercent(float authPercent) {
        this.authPercent = authPercent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }
}
