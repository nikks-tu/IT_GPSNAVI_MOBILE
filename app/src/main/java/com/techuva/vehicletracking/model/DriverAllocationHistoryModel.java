package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverAllocationHistoryModel {

    @SerializedName("VEHICLE_DRIVER_MAP_ID")
    @Expose
    private Integer vEHICLEDRIVERMAPID;
    @SerializedName("VEHICLE_MASTER_ID")
    @Expose
    private Integer vEHICLEMASTERID;
    @SerializedName("VEHICLE_DRIVER_ID")
    @Expose
    private Integer vEHICLEDRIVERID;
    @SerializedName("ASSIGNED_ON")
    @Expose
    private String aSSIGNEDON;
    @SerializedName("REVOKED_ON")
    @Expose
    private String rEVOKEDON;
    @SerializedName("REMARKS")
    @Expose
    private String rEMARKS;
    @SerializedName("IS_ACTIVE")
    @Expose
    private Boolean iSACTIVE;
    @SerializedName("CREATED_ON")
    @Expose
    private String cREATEDON;
    @SerializedName("LAST_MODIFIED_ON")
    @Expose
    private String lASTMODIFIEDON;
    @SerializedName("REGISTRATION_NO")
    @Expose
    private String rEGISTRATIONNO;
    @SerializedName("DRIVER_NAME")
    @Expose
    private String dRIVERNAME;
    @SerializedName("ASSIGNED_BY_NAME")
    @Expose
    private String aSSIGNEDBYNAME;
    @SerializedName("CREATED_BY_NAME")
    @Expose
    private String cREATEDBYNAME;
    @SerializedName("LAST_MODIFIED_BY_NAME")
    @Expose
    private String lASTMODIFIEDBYNAME;
    @SerializedName("REVOKED_BY_NAME")
    @Expose
    private String rEVOKEDBYNAME;

    public Integer getVEHICLEDRIVERMAPID() {
        return vEHICLEDRIVERMAPID;
    }

    public void setVEHICLEDRIVERMAPID(Integer vEHICLEDRIVERMAPID) {
        this.vEHICLEDRIVERMAPID = vEHICLEDRIVERMAPID;
    }

    public Integer getVEHICLEMASTERID() {
        return vEHICLEMASTERID;
    }

    public void setVEHICLEMASTERID(Integer vEHICLEMASTERID) {
        this.vEHICLEMASTERID = vEHICLEMASTERID;
    }

    public Integer getVEHICLEDRIVERID() {
        return vEHICLEDRIVERID;
    }

    public void setVEHICLEDRIVERID(Integer vEHICLEDRIVERID) {
        this.vEHICLEDRIVERID = vEHICLEDRIVERID;
    }

    public String getASSIGNEDON() {
        return aSSIGNEDON;
    }

    public void setASSIGNEDON(String aSSIGNEDON) {
        this.aSSIGNEDON = aSSIGNEDON;
    }

    public String getREVOKEDON() {
        return rEVOKEDON;
    }

    public void setREVOKEDON(String rEVOKEDON) {
        this.rEVOKEDON = rEVOKEDON;
    }

    public String getREMARKS() {
        return rEMARKS;
    }

    public void setREMARKS(String rEMARKS) {
        this.rEMARKS = rEMARKS;
    }

    public Boolean getISACTIVE() {
        return iSACTIVE;
    }

    public void setISACTIVE(Boolean iSACTIVE) {
        this.iSACTIVE = iSACTIVE;
    }

    public String getCREATEDON() {
        return cREATEDON;
    }

    public void setCREATEDON(String cREATEDON) {
        this.cREATEDON = cREATEDON;
    }

    public String getLASTMODIFIEDON() {
        return lASTMODIFIEDON;
    }

    public void setLASTMODIFIEDON(String lASTMODIFIEDON) {
        this.lASTMODIFIEDON = lASTMODIFIEDON;
    }

    public String getREGISTRATIONNO() {
        return rEGISTRATIONNO;
    }

    public void setREGISTRATIONNO(String rEGISTRATIONNO) {
        this.rEGISTRATIONNO = rEGISTRATIONNO;
    }

    public String getDRIVERNAME() {
        return dRIVERNAME;
    }

    public void setDRIVERNAME(String dRIVERNAME) {
        this.dRIVERNAME = dRIVERNAME;
    }

    public String getASSIGNEDBYNAME() {
        return aSSIGNEDBYNAME;
    }

    public void setASSIGNEDBYNAME(String aSSIGNEDBYNAME) {
        this.aSSIGNEDBYNAME = aSSIGNEDBYNAME;
    }

    public String getCREATEDBYNAME() {
        return cREATEDBYNAME;
    }

    public void setCREATEDBYNAME(String cREATEDBYNAME) {
        this.cREATEDBYNAME = cREATEDBYNAME;
    }

    public String getLASTMODIFIEDBYNAME() {
        return lASTMODIFIEDBYNAME;
    }

    public void setLASTMODIFIEDBYNAME(String lASTMODIFIEDBYNAME) {
        this.lASTMODIFIEDBYNAME = lASTMODIFIEDBYNAME;
    }

    public String getREVOKEDBYNAME() {
        return rEVOKEDBYNAME;
    }

    public void setREVOKEDBYNAME(String rEVOKEDBYNAME) {
        this.rEVOKEDBYNAME = rEVOKEDBYNAME;
    }

}