package com.techuva.vehicletracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryDataObject {

    @SerializedName("ChannelNumber")
    @Expose
    private String ChannelNumber;
    @SerializedName("Label")
    @Expose
    private String Label;
    @SerializedName("icon")
    @Expose
    private int icon;
    @SerializedName("display_order")
    @Expose
    private Integer display_order;
    @SerializedName("Value")
    @Expose
    private String Value;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

    public String getChannelNumber() {
        return ChannelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        ChannelNumber = channelNumber;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Integer getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(Integer display_order) {
        this.display_order = display_order;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}