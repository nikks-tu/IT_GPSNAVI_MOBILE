package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleSummaryObject {

    @SerializedName("REGISTRATION_NO")
    @Expose
    private String rEGISTRATIONNO;
    @SerializedName("TOTAL_DISTANCE")
    @Expose
    private Double tOTALDISTANCE;
    @SerializedName("CREATED_ON")
    @Expose
    private String cREATEDON;
    @SerializedName("STATUS")
    @Expose
    private Integer sTATUS;
    @SerializedName("VEHICLE_NO")
    @Expose
    private String vEHICLENO;

    public String getREGISTRATIONNO() {
        return rEGISTRATIONNO;
    }

    public void setREGISTRATIONNO(String rEGISTRATIONNO) {
        this.rEGISTRATIONNO = rEGISTRATIONNO;
    }

    public Double getTOTALDISTANCE() {
        return tOTALDISTANCE;
    }

    public void setTOTALDISTANCE(Double tOTALDISTANCE) {
        this.tOTALDISTANCE = tOTALDISTANCE;
    }

    public String getCREATEDON() {
        return cREATEDON;
    }

    public void setCREATEDON(String cREATEDON) {
        this.cREATEDON = cREATEDON;
    }

    public Integer getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(Integer sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getVEHICLENO() {
        return vEHICLENO;
    }

    public void setVEHICLENO(String vEHICLENO) {
        this.vEHICLENO = vEHICLENO;
    }

}