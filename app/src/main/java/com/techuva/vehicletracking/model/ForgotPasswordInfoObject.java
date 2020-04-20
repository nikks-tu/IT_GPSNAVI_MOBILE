package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordInfoObject  {

    @SerializedName("listCount")
    @Expose
    private Integer listCount;
    @SerializedName("errorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("pageNumber")
    @Expose
    private Object pageNumber;
    @SerializedName("pagePerCount")
    @Expose
    private Object pagePerCount;
    @SerializedName("fromRecords")
    @Expose
    private Integer fromRecords;
    @SerializedName("toRecords")
    @Expose
    private Integer toRecords;
    @SerializedName("totalRecords")
    @Expose
    private Integer totalRecords;

    public Integer getListCount() {
        return listCount;
    }

    public void setListCount(Integer listCount) {
        this.listCount = listCount;
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

    public Object getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Object pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Object getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(Object pagePerCount) {
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

}