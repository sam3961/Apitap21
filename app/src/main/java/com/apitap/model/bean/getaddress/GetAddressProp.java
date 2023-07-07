
package com.apitap.model.bean.getaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAddressProp {


    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = new ArrayList<RESULT>();

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        this.rESULT = rESULT;
    }

}
