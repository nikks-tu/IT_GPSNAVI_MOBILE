package com.techuva.vehicletracking.model;

public class GetDeviceListPostParameters {

    String searchKey;
    String pagePerCount;
    int pageNumber;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(String pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }


    public GetDeviceListPostParameters(String searchKey, String pagePerCount, int pageNumber) {
        this.searchKey = searchKey;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }

}
