package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableDevicesModel {

    @SerializedName("INVENTORY_ID")
    @Expose
    private Integer iNVENTORYID;
    @SerializedName("DISPLAY_NAME")
    @Expose
    private String dISPLAYNAME;
    @SerializedName("STATUS_ID")
    @Expose
    private Integer sTATUSID;
    @SerializedName("DESCRIPTION")
    @Expose
    private String dESCRIPTION;
    @SerializedName("SERIAL_NO")
    @Expose
    private String sERIALNO;

    public Integer getINVENTORYID() {
        return iNVENTORYID;
    }

    public void setINVENTORYID(Integer iNVENTORYID) {
        this.iNVENTORYID = iNVENTORYID;
    }

    public String getDISPLAYNAME() {
        return dISPLAYNAME;
    }

    public void setDISPLAYNAME(String dISPLAYNAME) {
        this.dISPLAYNAME = dISPLAYNAME;
    }

    public Integer getSTATUSID() {
        return sTATUSID;
    }

    public void setSTATUSID(Integer sTATUSID) {
        this.sTATUSID = sTATUSID;
    }

    public String getDESCRIPTION() {
        return dESCRIPTION;
    }

    public void setDESCRIPTION(String dESCRIPTION) {
        this.dESCRIPTION = dESCRIPTION;
    }

    public String getSERIALNO() {
        return sERIALNO;
    }

    public void setSERIALNO(String sERIALNO) {
        this.sERIALNO = sERIALNO;
    }

}