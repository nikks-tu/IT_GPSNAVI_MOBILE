package com.techuva.vehicletracking.api_interface;

import com.google.gson.JsonElement;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.model.HistoryDataMainObject;
import com.techuva.vehicletracking.model.HistoryDataPostParamter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface HistoryDataInterface {

    @POST(Constants.HistoryData)
    Call<HistoryDataMainObject>  getStringScalar(@Body HistoryDataPostParamter postParameter);

    @POST(Constants.CurrentData)
    Call<JsonElement>  getCurrentData(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body HistoryDataPostParamter postParameter);

    @POST(Constants.HistoryData)
    Call<JsonElement>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body HistoryDataPostParamter postParameter);

    @POST(Constants.GetVehicalSummary)
    Call<JsonElement>  getVehicleSummary(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body HistoryDataPostParamter postParameter);

}
