package com.techuva.vehicletracking.api_interface;
import com.google.gson.JsonElement;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.model.VersionInfoMainObject;
import com.techuva.vehicletracking.model.VersionInfoPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface VersionInfoDataInterface {
    @POST(Constants.VersionCheck)
    Call<JsonElement>  getStringScalar(@Body VersionInfoPostParameters postParameter);

    @POST(Constants.VersionCheck)
    Call<VersionInfoMainObject>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body VersionInfoPostParameters postParameter);
}
