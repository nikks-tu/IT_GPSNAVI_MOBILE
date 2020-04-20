package com.techuva.vehicletracking.model;

public class DeviceAllocationHistoryPostParameters {
    int vehicleMasterId;
    String searchKey;
    String pagePerCount;
    int pageNumber;

    public DeviceAllocationHistoryPostParameters(int vehicleMasterId, String searchKey, String pagePerCount, int pageNumber) {
        this.vehicleMasterId = vehicleMasterId;
        this.searchKey = searchKey;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }


    public int getVehicleMasterId() {
        return vehicleMasterId;
    }

    public void setVehicleMasterId(int vehicleMasterId) {
        this.vehicleMasterId = vehicleMasterId;
    }

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

}
