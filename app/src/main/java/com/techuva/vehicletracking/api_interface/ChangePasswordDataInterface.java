package com.techuva.vehicletracking.api_interface;


import com.google.gson.JsonElement;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.model.LoginPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChangePasswordDataInterface {

    @POST(Constants.ChangePassword)
    Call<JsonElement> getStringScalar(@Header("authUser") int headerValue, @Body LoginPostParameters postParameter);

    @POST(Constants.ChangePassword)
    Call<JsonElement> getStringScalar(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body LoginPostParameters postParameter);

}
