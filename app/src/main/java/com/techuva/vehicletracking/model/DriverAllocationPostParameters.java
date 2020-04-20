package com.techuva.vehicletracking.model;

public class DriverAllocationPostParameters {


    public DriverAllocationPostParameters(int VEHICLE_MASTER_ID, int VEHICLE_DRIVER_ID, String REMARKS) {
        this.VEHICLE_MASTER_ID = VEHICLE_MASTER_ID;
        this.VEHICLE_DRIVER_ID = VEHICLE_DRIVER_ID;
        this.REMARKS = REMARKS;
    }

    public int getVEHICLE_MASTER_ID() {
        return VEHICLE_MASTER_ID;
    }

    public void setVEHICLE_MASTER_ID(int VEHICLE_MASTER_ID) {
        this.VEHICLE_MASTER_ID = VEHICLE_MASTER_ID;
    }

    public int getVEHICLE_DRIVER_ID() {
        return VEHICLE_DRIVER_ID;
    }

    public void setVEHICLE_DRIVER_ID(int VEHICLE_DRIVER_ID) {
        this.VEHICLE_DRIVER_ID = VEHICLE_DRIVER_ID;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    int VEHICLE_MASTER_ID;
    int VEHICLE_DRIVER_ID;
    String REMARKS;


}
