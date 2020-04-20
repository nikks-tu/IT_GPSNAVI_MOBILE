package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SevenDaysDistanceModel  {

    @SerializedName("REGISTRATION_NO")
    @Expose
    private String rEGISTRATIONNO;
    @SerializedName("TOTAL_DISTANCE")
    @Expose
    private Double tOTALDISTANCE;
    @SerializedName("TRAVELLED_ON")
    @Expose
    private String tRAVELLEDON;

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

    public String getTRAVELLEDON() {
        return tRAVELLEDON;
    }

    public void setTRAVELLEDON(String tRAVELLEDON) {
        this.tRAVELLEDON = tRAVELLEDON;
    }

}