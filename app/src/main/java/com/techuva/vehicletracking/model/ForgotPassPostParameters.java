package com.techuva.vehicletracking.model;

public class ForgotPassPostParameters {
    private String email;


    public ForgotPassPostParameters(String emailId) {
        email = emailId;
    }

    public String getEmailId() {
        return email;
    }

    public void setEmailId(String emailId) {
        email = emailId;
    }


}
