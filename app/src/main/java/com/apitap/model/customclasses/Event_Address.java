package com.apitap.model.customclasses;


import com.apitap.model.bean.getaddress.AddressData;
import com.apitap.model.bean.getaddress.RESULT;

import java.util.List;

/**
 * Created by ashok-kumar on 4/8/16.
 */

public class Event_Address {
    private int key;
    private boolean isCode;
    private List<AddressData> response_addresses;
    private RESULT.RESULT_ response_code;

    public boolean isCode() {
        return isCode;
    }

    public void setCode(boolean code) {
        isCode = code;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public List<AddressData> getResponse() {
        return response_addresses;
    }

    public void setResponse(List<AddressData> response) {
        this.response_addresses = response;
    }

    public RESULT.RESULT_ getResponse_code() {
        return response_code;
    }

    public void setResponse_code(RESULT.RESULT_ response_code) {
        this.response_code = response_code;
    }
}
