
package com.apitap.model.bean.getaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityData {


    @SerializedName("_114_14")
    @Expose
    private String cityId;
    @SerializedName("_47_15")
    @Expose
    private String cityName;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}