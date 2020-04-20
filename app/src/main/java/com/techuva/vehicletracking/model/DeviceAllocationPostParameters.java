package com.techuva.vehicletracking.model;

public class DeviceAllocationPostParameters {

    public DeviceAllocationPostParameters(int VEHICLE_MASTER_ID, int INVENTORY_ID, String REMARKS) {
        this.VEHICLE_MASTER_ID = VEHICLE_MASTER_ID;
        this.INVENTORY_ID = INVENTORY_ID;
        this.REMARKS = REMARKS;
    }


    int VEHICLE_MASTER_ID;
    int INVENTORY_ID;
    String REMARKS;

    public int getVEHICLE_MASTER_ID() {
        return VEHICLE_MASTER_ID;
    }

    public void setVEHICLE_MASTER_ID(int VEHICLE_MASTER_ID) {
        this.VEHICLE_MASTER_ID = VEHICLE_MASTER_ID;
    }

    public int getINVENTORY_ID() {
        return INVENTORY_ID;
    }

    public void setINVENTORY_ID(int INVENTORY_ID) {
        this.INVENTORY_ID = INVENTORY_ID;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

}
