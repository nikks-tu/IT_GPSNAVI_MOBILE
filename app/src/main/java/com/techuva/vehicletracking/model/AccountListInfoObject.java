package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountListInfoObject  {

    @SerializedName("listCount")
    @Expose
    private Integer listCount;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("pagePerCount")
    @Expose
    private Integer pagePerCount;
    @SerializedName("fromRecords")
    @Expose
    private Integer fromRecords;
    @SerializedName("toRecords")
    @Expose
    private Integer toRecords;
    @SerializedName("totalRecords")
    @Expose
    private Integer totalRecords;
    @SerializedName("errorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public Integer getListCount() {
        return listCount;
    }

    public void setListCount(Integer listCount) {
        this.listCount = listCount;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(Integer pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public Integer getFromRecords() {
        return fromRecords;
    }

    public void setFromRecords(Integer fromRecords) {
        this.fromRecords = fromRecords;
    }

    public Integer getToRecords() {
        return toRecords;
    }

    public void setToRecords(Integer toRecords) {
        this.toRecords = toRecords;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}