package com.techuva.vehicletracking.api_interface;

import com.google.gson.JsonElement;
import com.techuva.vehicletracking.model.DeviceDeallocationPostParameters;
import com.techuva.vehicletracking.model.DriverDeallocationPostParameters;
import com.techuva.vehicletracking.model.ForgotPassPostParameters;
import com.techuva.vehicletracking.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface DeallocationDataInterface {


    @POST(Constants.DeallocateDevice)
    Call<JsonElement> deviceDeallocationCall(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DeviceDeallocationPostParameters postParameter);


    @POST(Constants.DelocateDriver)
    Call<JsonElement> driverDeallocationCall(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DriverDeallocationPostParameters postParameter);

}
