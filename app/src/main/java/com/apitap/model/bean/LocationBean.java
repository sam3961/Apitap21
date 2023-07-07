package com.apitap.model.bean;

/**
 * Created by rishav on 27/9/17.
 */

public class LocationBean {


    private String nameapitap;
    private String addresline;
    private String addresstwo;
    private String number;
    private String lats;
    private String longs;
    private String merchantName;
    private String distance;

    public LocationBean(String name, String addresline, String addresstwo, String number,String lats,String longs,String merchantName,String distance) {
        this.nameapitap = name;
        this.addresline = addresline;
        this.addresstwo = addresstwo;
        this.number = number;
        this.longs = longs;
        this.lats = lats;
        this.distance = distance;
        this.merchantName = merchantName;
    }
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getNameapitap() {
        return nameapitap;
    }

    public void setNameapitap(String nameapitap) {
        this.nameapitap = nameapitap;
    }


    public String getLats() {
        return lats;
    }

    public void setLats(String lats) {
        this.lats = lats;
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs;
    }


    public String getAddresline() {
        return addresline;
    }

    public void setAddresline(String addresline) {
        this.addresline = addresline;
    }

    public String getAddresstwo() {
        return addresstwo;
    }

    public void setAddresstwo(String addresstwo) {
        this.addresstwo = addresstwo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }



}
