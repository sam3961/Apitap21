package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class GetDeliveryMerchantBean implements Parcelable{

    private String id;
    private String delviery_options;
    private String delivery_tier;
    private String delivery_price;
    private String imageUrl;
    private String pickup;




    public GetDeliveryMerchantBean(){

    }

    protected GetDeliveryMerchantBean(Parcel in) {
        id = in.readString();
        delviery_options = in.readString();
        delivery_tier = in.readString();
        delivery_price = in.readString();
        imageUrl = in.readString();
        pickup = in.readString();
    }

    public static final Creator<GetDeliveryMerchantBean> CREATOR = new Creator<GetDeliveryMerchantBean>() {
        @Override
        public GetDeliveryMerchantBean createFromParcel(Parcel in) {
            return new GetDeliveryMerchantBean(in);
        }

        @Override
        public GetDeliveryMerchantBean[] newArray(int size) {
            return new GetDeliveryMerchantBean[size];
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

    public String getDelviery_options() {
        return delviery_options;
    }

    public void setDelviery_options(String delviery_options) {
        this.delviery_options = delviery_options;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getPickup() {
        return pickup;
    }



    public String getDelivery_tier() {
        return delivery_tier;
    }

    public void setDelivery_tier(String actualPrice) {
        this.delivery_tier = actualPrice;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String priceAfterDiscount) {
        this.delivery_price = priceAfterDiscount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(delviery_options);
        parcel.writeString(delivery_tier);
        parcel.writeString(delivery_price);
        parcel.writeString(imageUrl);
        parcel.writeString(pickup);
    }
}
