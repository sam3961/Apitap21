package com.apitap.model.bean;

import java.util.ArrayList;

/**
 * Created by sourcefuse on 8/9/16.
 */

public class Sizedata {

    private String name;
    private ArrayList<SizeBean> sizeArray;

    public ArrayList<SizeBean> getSizeArray() {
        return sizeArray;
    }

    public void setSizeArray(ArrayList<SizeBean> sizeArray) {
        this.sizeArray = sizeArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
