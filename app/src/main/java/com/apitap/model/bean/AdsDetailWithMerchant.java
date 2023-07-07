package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class AdsDetailWithMerchant implements Parcelable{

    private String id;
    private String AdId;
    private String merchantId;
    private String name;
    private String Merchantname;
    private String isSeen;
    private String imageUrl;
    private String video;
    private String desc;
    private String business_type;
    private String actualPrice;
    private String priceAfterDiscount;

    public AdsDetailWithMerchant(){

    }

    protected AdsDetailWithMerchant(Parcel in) {
        id = in.readString();
        merchantId = in.readString();
        name = in.readString();
        Merchantname=in.readString();
        actualPrice = in.readString();
        priceAfterDiscount = in.readString();
        imageUrl = in.readString();
        video = in.readString();
        desc = in.readString();
        isSeen = in.readString();
        business_type = in.readString();
        AdId = in.readString();
    }

    public static final Creator<AdsDetailWithMerchant> CREATOR = new Creator<AdsDetailWithMerchant>() {
        @Override
        public AdsDetailWithMerchant createFromParcel(Parcel in) {
            return new AdsDetailWithMerchant(in);
        }

        @Override
        public AdsDetailWithMerchant[] newArray(int size) {
            return new AdsDetailWithMerchant[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantname() {
        return Merchantname;
    }
    public String getMerchantId() {
        return merchantId;
    }

    public void setVideo(String name) {
        this.video = name;
    }

    public void setBusinssType(String name) {
        this.business_type = name;
    }
    public String getBusiness_type() {
        return business_type;
    }


    public String getVideo() {
        return video;
    }

    public String getAdId() {
        return AdId;
    }

    public void setAdId(String adId) {
        AdId = adId;
    }

    public void setMerchantname(String merchantname) {
        this.Merchantname = merchantname;
    }

    public void setSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(merchantId);
        parcel.writeString(name);
        parcel.writeString(Merchantname);
        parcel.writeString(actualPrice);
        parcel.writeString(priceAfterDiscount);
        parcel.writeString(imageUrl);
        parcel.writeString(video);
        parcel.writeString(desc);
        parcel.writeString(isSeen);
        parcel.writeString(business_type);
        parcel.writeString(AdId);
    }


}
