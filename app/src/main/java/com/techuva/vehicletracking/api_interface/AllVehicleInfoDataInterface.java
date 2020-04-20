package com.techuva.vehicletracking.api_interface;

import com.google.gson.JsonElement;
import com.techuva.vehicletracking.model.DeviceAllocationHistoryPostParameters;
import com.techuva.vehicletracking.model.DeviceAllocationPostParameters;
import com.techuva.vehicletracking.model.DeviceReAllocationPostParameters;
import com.techuva.vehicletracking.model.DriverAllocationPostParameters;
import com.techuva.vehicletracking.model.GetDeviceListPostParameters;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.model.GetAllVehicleInfoPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface AllVehicleInfoDataInterface {

    @POST(Constants.GetAllVehicleInfo)
    Call<JsonElement>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body GetAllVehicleInfoPostParameters postParameter);

    @GET(Constants.GetCurrentCount)
    Call<JsonElement> getCurrentVehicleCount(@Header("authUser") int headerValue, @Header("authorization") String authorization);

    @GET("Distance/getSingleVehicleInfo/{RegistrationNum}")
    Call<JsonElement> getSingleVehicleDetails(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Path("RegistrationNum") String RegistrationNum);

    @GET("Distance/getLastSevenDaysInfo/{RegistrationNum}")
    Call<JsonElement> getSvenDaysRecords(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Path("RegistrationNum") String RegistrationNum);

    @POST(Constants.GetDeviceAllocationHistory)
    Call<JsonElement>  getDeviceAllocationHistory(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DeviceAllocationHistoryPostParameters postParameter);

    @POST(Constants.GetDriverAllocationHistory)
    Call<JsonElement>  getDriverAllocationHistory(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DeviceAllocationHistoryPostParameters postParameter);

    @POST(Constants.GetAvailableDeviceList)
    Call<JsonElement>  getDeviceList(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body GetDeviceListPostParameters postParameter);

    @POST(Constants.AllocateVehicleDevice)
    Call<JsonElement>  allocateDevice(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DeviceAllocationPostParameters postParameter);

    @POST(Constants.ReAllocateVehicleDevice)
    Call<JsonElement>  reAllocateDevice(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DeviceReAllocationPostParameters postParameter);

    @POST(Constants.GetAvailableDriversList)
    Call<JsonElement>  getDriversList(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body GetDeviceListPostParameters postParameter);

    @POST(Constants.AllocateDriver)
    Call<JsonElement>  allocateDriver(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DriverAllocationPostParameters postParameter);

    @POST(Constants.ReAllocateDriver)
    Call<JsonElement>  reAllocateDriver(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DriverAllocationPostParameters postParameter);


}
