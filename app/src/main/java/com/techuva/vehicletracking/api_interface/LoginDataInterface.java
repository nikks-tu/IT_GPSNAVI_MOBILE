package com.techuva.vehicletracking.api_interface;

import com.google.gson.JsonElement;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.model.LoginMainObject;
import com.techuva.vehicletracking.model.LoginPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface LoginDataInterface {

    @POST(Constants.LoginData)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<LoginMainObject> getStringScalar(@Body LoginPostParameters postParameter);

    @FormUrlEncoded
    @POST(Constants.LoginData)
    Call<LoginMainObject> loginCall(@Header("authorization") String authorization, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @POST(Constants.LoginData)
    Call<LoginMainObject> refreshSession(@Header("authorization") String authorization, @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token);



}
