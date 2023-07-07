
package com.apitap.model.bean.getaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryData {


    @SerializedName("_122_87")
    @Expose
    private String countryId;
    @SerializedName("_47_18")
    @Expose
    private String countryName;
    @SerializedName("_114_17")
    @Expose
    private String countryCode;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}