package com.techuva.vehicletracking.api_interface;

import com.google.gson.JsonElement;
import com.techuva.vehicletracking.model.ForgotPassPostParameters;
import com.techuva.vehicletracking.model.SignUpPostParameters;
import com.techuva.vehicletracking.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface SignUpDataInterface {

    @POST(Constants.SignUp)
    Call<JsonElement> getStringScalar(@Body SignUpPostParameters postParameter);

}
