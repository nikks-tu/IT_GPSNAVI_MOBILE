package com.techuva.vehicletracking.api_interface;


import com.techuva.vehicletracking.model.AccountsListMainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


public interface AccountDetailsInterface {

    //@GET(Constants.CompanyList+"{UserID}")
    @GET("TUUserManagement/api/user/companydashboard/{UserID}")
    Call<AccountsListMainObject> getUserAccountDetails(@Path("UserID") int UserID);

    @GET("companydashboard/{UserID}")
    Call<AccountsListMainObject> getUserAccountDetailsWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Path("UserID") int UserID);

}
