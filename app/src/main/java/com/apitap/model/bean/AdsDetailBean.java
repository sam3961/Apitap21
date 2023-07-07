package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class AdsDetailBean implements Parcelable{

    private String id;
    private String name;
    private String seen;
    private String actualPrice;
    private String priceAfterDiscount;
    private String imageUrl;
    private String merchantName;
    private String AdName;
    private String business_type;



    public AdsDetailBean(){

    }

    protected AdsDetailBean(Parcel in) {
        id = in.readString();
        name = in.readString();
        actualPrice = in.readString();
        priceAfterDiscount = in.readString();
        imageUrl = in.readString();
        seen = in.readString();
        business_type = in.readString();
        merchantName = in.readString();
    }

    public static final Creator<AdsDetailBean> CREATOR = new Creator<AdsDetailBean>() {
        @Override
        public AdsDetailBean createFromParcel(Parcel in) {
            return new AdsDetailBean(in);
        }

        @Override
        public AdsDetailBean[] newArray(int size) {
            return new AdsDetailBean[size];
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

    public String getName() {
        return name;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }


    public void setName(String name) {
        this.name = name;
    }




    public String getActualPrice() {
        return actualPrice;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(actualPrice);
        parcel.writeString(priceAfterDiscount);
        parcel.writeString(imageUrl);
        parcel.writeString(business_type);
        parcel.writeString(seen);
        parcel.writeString(merchantName);
    }
}
