package com.techuva.vehicletracking.model;

public class DeviceReAllocationPostParameters {

    public DeviceReAllocationPostParameters(int VEHICLE_MASTER_ID, int INVENTORY_ID, String REMARKS) {
        this.VEHICLE_TAG_MAP_ID = VEHICLE_MASTER_ID;
        this.INVENTORY_ID = INVENTORY_ID;
        this.REMARKS = REMARKS;
    }

    int VEHICLE_TAG_MAP_ID;

    public int getVEHICLE_TAG_MAP_ID() {
        return VEHICLE_TAG_MAP_ID;
    }

    public void setVEHICLE_TAG_MAP_ID(int VEHICLE_TAG_MAP_ID) {
        this.VEHICLE_TAG_MAP_ID = VEHICLE_TAG_MAP_ID;
    }

    int INVENTORY_ID;
    String REMARKS;

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
