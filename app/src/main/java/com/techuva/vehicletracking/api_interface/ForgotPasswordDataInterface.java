package com.techuva.vehicletracking.api_interface;

import com.google.gson.JsonElement;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.model.ForgotPassPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ForgotPasswordDataInterface {

    @POST(Constants.ForgetPassword)
    Call<JsonElement> getStringScalar(@Body ForgotPassPostParameters postParameter);

}
