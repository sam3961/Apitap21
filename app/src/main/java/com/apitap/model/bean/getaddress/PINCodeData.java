
package com.apitap.model.bean.getaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PINCodeData {


    @SerializedName("_122_107")
    @Expose
    private String pinId;
    @SerializedName("_47_17")
    @Expose
    private String pinCode;

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}