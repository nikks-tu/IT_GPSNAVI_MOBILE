package com.techuva.vehicletracking.model;

public class AvailableDriversListModel {

    public int getVEHICLE_DRIVER_ID() {
        return VEHICLE_DRIVER_ID;
    }

    public void setVEHICLE_DRIVER_ID(int VEHICLE_DRIVER_ID) {
        this.VEHICLE_DRIVER_ID = VEHICLE_DRIVER_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }


    int VEHICLE_DRIVER_ID;
    String NAME;
    String MOBILE_NO;
}
