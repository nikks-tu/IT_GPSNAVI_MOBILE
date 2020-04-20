package com.techuva.vehicletracking.model;

public class SignUpPostParameters {

    String firstName;
    String lastName;
    String mobileNo;

    public SignUpPostParameters(String firstName, String lastName, String mobileNo, String emailId, String devSerialNo, String regdCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.devSerialNo = devSerialNo;
        this.regdCode = regdCode;
    }

    String emailId;
    String devSerialNo;
    String regdCode;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDevSerialNo() {
        return devSerialNo;
    }

    public void setDevSerialNo(String devSerialNo) {
        this.devSerialNo = devSerialNo;
    }

    public String getRegdCode() {
        return regdCode;
    }

    public void setRegdCode(String regdCode) {
        this.regdCode = regdCode;
    }
}
