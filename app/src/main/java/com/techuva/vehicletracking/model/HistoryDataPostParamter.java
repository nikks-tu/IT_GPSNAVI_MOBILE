package com.techuva.vehicletracking.model;

public class HistoryDataPostParamter {

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    private String vehicleNo;
    private String from_date;
    private String to_date;
    private String pagePerCount;
    private String pageNumber;

    int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    int year;

    public HistoryDataPostParamter(int month, int year, String registrationNo) {
        this.month = month;
        this.year = year;
        this.registrationNo = registrationNo;
    }

    String registrationNo;
    public HistoryDataPostParamter(String deviceId) {
        this.vehicleNo = deviceId;
    }

    public HistoryDataPostParamter(String vehicleNo, String from_date, String to_date, String pagePerCount, String pageNumber) {
        this.vehicleNo = vehicleNo;
        this.from_date = from_date;
        this.to_date = to_date;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(String pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }


    public String getFromDate() {
        return from_date;
    }

    public void setFromDate(String fromDate) {
        this.from_date = fromDate;
    }

    public String getToDate() {
        return to_date;
    }

    public void setToDate(String toDate) {
        this.to_date = toDate;
    }


}
