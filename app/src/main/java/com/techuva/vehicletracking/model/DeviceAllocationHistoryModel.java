package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceAllocationHistoryModel {

    @SerializedName("VEHICLE_TAG_MAP_ID")
    @Expose
    private Integer vEHICLETAGMAPID;
    @SerializedName("VEHICLE_MASTER_ID")
    @Expose
    private Integer vEHICLEMASTERID;
    @SerializedName("INVENTORY_ID")
    @Expose
    private Integer iNVENTORYID;
    @SerializedName("TAG_ASSIGNED_ON")
    @Expose
    private String tAGASSIGNEDON;
    @SerializedName("TAG_REVOKED_ON")
    @Expose
    private String tAGREVOKEDON;
    @SerializedName("IS_ACTIVE")
    @Expose
    private Boolean iSACTIVE;
    @SerializedName("CREATED_ON")
    @Expose
    private String cREATEDON;
    @SerializedName("LAST_MODIFIED_ON")
    @Expose
    private String lASTMODIFIEDON;
    @SerializedName("TAG_NAME")
    @Expose
    private String tAGNAME;
    @SerializedName("REGISTRATION_NO")
    @Expose
    private String rEGISTRATIONNO;
    @SerializedName("VEHICLE_MODEL_DESCRIPTION")
    @Expose
    private String vEHICLEMODELDESCRIPTION;
    @SerializedName("CREATED_BY_NAME")
    @Expose
    private String cREATEDBYNAME;
    @SerializedName("LAST_MODIFIED_BY_NAME")
    @Expose
    private String lASTMODIFIEDBYNAME;
    @SerializedName("TAG_ASSIGNED_BY_NAME")
    @Expose
    private String tAGASSIGNEDBYNAME;
    @SerializedName("TAG_REVOKED_BY_NAME")
    @Expose
    private String tAGREVOKEDBYNAME;
    @SerializedName("REMARKS")
    @Expose
    private String rEMARKS;

    public Integer getVEHICLETAGMAPID() {
        return vEHICLETAGMAPID;
    }

    public void setVEHICLETAGMAPID(Integer vEHICLETAGMAPID) {
        this.vEHICLETAGMAPID = vEHICLETAGMAPID;
    }

    public Integer getVEHICLEMASTERID() {
        return vEHICLEMASTERID;
    }

    public void setVEHICLEMASTERID(Integer vEHICLEMASTERID) {
        this.vEHICLEMASTERID = vEHICLEMASTERID;
    }

    public Integer getINVENTORYID() {
        return iNVENTORYID;
    }

    public void setINVENTORYID(Integer iNVENTORYID) {
        this.iNVENTORYID = iNVENTORYID;
    }

    public String getTAGASSIGNEDON() {
        return tAGASSIGNEDON;
    }

    public void setTAGASSIGNEDON(String tAGASSIGNEDON) {
        this.tAGASSIGNEDON = tAGASSIGNEDON;
    }

    public String getTAGREVOKEDON() {
        return tAGREVOKEDON;
    }

    public void setTAGREVOKEDON(String tAGREVOKEDON) {
        this.tAGREVOKEDON = tAGREVOKEDON;
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

    public String getTAGNAME() {
        return tAGNAME;
    }

    public void setTAGNAME(String tAGNAME) {
        this.tAGNAME = tAGNAME;
    }

    public String getREGISTRATIONNO() {
        return rEGISTRATIONNO;
    }

    public void setREGISTRATIONNO(String rEGISTRATIONNO) {
        this.rEGISTRATIONNO = rEGISTRATIONNO;
    }

    public String getVEHICLEMODELDESCRIPTION() {
        return vEHICLEMODELDESCRIPTION;
    }

    public void setVEHICLEMODELDESCRIPTION(String vEHICLEMODELDESCRIPTION) {
        this.vEHICLEMODELDESCRIPTION = vEHICLEMODELDESCRIPTION;
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

    public String getTAGASSIGNEDBYNAME() {
        return tAGASSIGNEDBYNAME;
    }

    public void setTAGASSIGNEDBYNAME(String tAGASSIGNEDBYNAME) {
        this.tAGASSIGNEDBYNAME = tAGASSIGNEDBYNAME;
    }

    public String getTAGREVOKEDBYNAME() {
        return tAGREVOKEDBYNAME;
    }

    public void setTAGREVOKEDBYNAME(String tAGREVOKEDBYNAME) {
        this.tAGREVOKEDBYNAME = tAGREVOKEDBYNAME;
    }

    public String getREMARKS() {
        return rEMARKS;
    }

    public void setREMARKS(String rEMARKS) {
        this.rEMARKS = rEMARKS;
    }

}