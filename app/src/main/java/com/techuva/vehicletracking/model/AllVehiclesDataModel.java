package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllVehiclesDataModel  {
    @SerializedName("VEHICLE_ID")
    @Expose
    private Integer vEHICLEID;
    @SerializedName("RegistrationNumber")
    @Expose
    private String registrationNumber;
    @SerializedName("VehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("DRIVER_NAME")
    @Expose
    private String dRIVERNAME;
    @SerializedName("StatusID")
    @Expose
    private Integer statusID;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("INVENTORY_ID")
    @Expose
    private Integer iNVENTORYID;
    @SerializedName("DeviceSerial")
    @Expose
    private String deviceSerial;
    @SerializedName("TOTAL_DISTANCE")
    @Expose
    private double tOTALDISTANCE;
    @SerializedName("ENTIRE_DISTANCE")
    @Expose
    private double eNTIRE_DISTANCE;
    @SerializedName("VEHICLE_TAG_MAP_ID")
    @Expose
    private Integer vEHICLETAGMAPID;
    @SerializedName("VEHICLE_DRIVER_ID")
    @Expose
    private Integer vEHICLEDRIVERID;
    @SerializedName("COMPANY_ID")
    @Expose
    private Integer cOMPANYID;
    @SerializedName("LAT")
    @Expose
    private Double lAT;
    @SerializedName("LANG")
    @Expose
    private Double lANG;
    @SerializedName("LastSyncOn")
    @Expose
    private String lastSyncOn;
    @SerializedName("ConditionId")
    @Expose
    private Integer conditionId;
    @SerializedName("Condition")
    @Expose
    private String condition;

    public Integer getVEHICLEID() {
        return vEHICLEID;
    }

    public void setVEHICLEID(Integer vEHICLEID) {
        this.vEHICLEID = vEHICLEID;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDRIVERNAME() {
        return dRIVERNAME;
    }

    public void setDRIVERNAME(String dRIVERNAME) {
        this.dRIVERNAME = dRIVERNAME;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getINVENTORYID() {
        return iNVENTORYID;
    }

    public void setINVENTORYID(Integer iNVENTORYID) {
        this.iNVENTORYID = iNVENTORYID;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public Integer getVEHICLETAGMAPID() {
        return vEHICLETAGMAPID;
    }

    public void setVEHICLETAGMAPID(Integer vEHICLETAGMAPID) {
        this.vEHICLETAGMAPID = vEHICLETAGMAPID;
    }

    public Integer getVEHICLEDRIVERID() {
        return vEHICLEDRIVERID;
    }

    public void setVEHICLEDRIVERID(Integer vEHICLEDRIVERID) {
        this.vEHICLEDRIVERID = vEHICLEDRIVERID;
    }

    public Integer getCOMPANYID() {
        return cOMPANYID;
    }

    public void setCOMPANYID(Integer cOMPANYID) {
        this.cOMPANYID = cOMPANYID;
    }

    public Double getLAT() {
        return lAT;
    }

    public void setLAT(Double lAT) {
        this.lAT = lAT;
    }

    public Double getLANG() {
        return lANG;
    }

    public void setLANG(Double lANG) {
        this.lANG = lANG;
    }

    public String getLastSyncOn() {
        return lastSyncOn;
    }

    public void setLastSyncOn(String lastSyncOn) {
        this.lastSyncOn = lastSyncOn;
    }

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double gettOTALDISTANCE() {
        return tOTALDISTANCE;
    }

    public void settOTALDISTANCE(double tOTALDISTANCE) {
        this.tOTALDISTANCE = tOTALDISTANCE;
    }

    public double geteNTIRE_DISTANCE() {
        return eNTIRE_DISTANCE;
    }

    public void seteNTIRE_DISTANCE(double eNTIRE_DISTANCE) {
        this.eNTIRE_DISTANCE = eNTIRE_DISTANCE;
    }


}