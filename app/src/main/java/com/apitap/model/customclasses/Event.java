package com.apitap.model.customclasses;

/**
 * Created by ashok-kumar on 4/8/16.
 */

public class Event {
    private int key;
    private String response;
    private String value1,value2,value3,value4;
    private Boolean hasData;

    public Event(int key, String response) {
        this.key = key;
        this.response = response;
    }
    public Event(int key, boolean hasData) {
        this.key = key;
        this.hasData = hasData;
    }
    public Event(int key, String value1,String value2,String value3,String value4) {
        this.key = key;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public Boolean hasData() {
        return hasData;
    }

    public int getKey() {
        return key;
    }

    public String getResponse() {
        return response;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public String getValue3() {
        return value3;
    }

    public String getValue4() {
        return value4;
    }
}
