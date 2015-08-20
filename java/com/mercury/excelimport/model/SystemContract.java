package com.mercury.excelimport.model;

import java.util.Date;

/**
 * Created by Fedake on 2015-08-20.
 */
public class SystemContract
{
    private int id;
    private boolean active;
    private float amount;
    private String amountPeriod;
    private String amountType;
    private float authPercent;
    private Date fromDate;
    private String orderNumber;
    private String request;
    private Date toDate;
    private int systemId;

    public SystemContract()
    {

    }

    public SystemContract(int id, boolean active, float amount, String amountPeriod, String amountType, float authPercent, Date fromDate, String orderNumber, String request, Date toDate, int systemId)
    {
        this.id = id;
        this.active = active;
        this.amount = amount;
        this.amountPeriod = amountPeriod;
        this.amountType = amountType;
        this.authPercent = authPercent;
        this.fromDate = fromDate;
        this.orderNumber = orderNumber;
        this.request = request;
        this.toDate = toDate;
        this.systemId = systemId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getAmountPeriod() {
        return amountPeriod;
    }

    public void setAmountPeriod(String amountPeriod) {
        this.amountPeriod = amountPeriod;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public float getAuthPercent() {
        return authPercent;
    }

    public void setAuthPercent(float authPercent) {
        this.authPercent = authPercent;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }
}
