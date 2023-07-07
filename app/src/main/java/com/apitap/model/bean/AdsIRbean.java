package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class AdsIRbean implements Parcelable{

    private String id;
    private String name;
    private String seen;
    private String Description;
    private String actualPrice;
    private String priceAfterDiscount;
    private String imageUrl;
    private String merchantName;
    private String productType;
    private String business_type;



    public AdsIRbean(){

    }

    protected AdsIRbean(Parcel in) {
        id = in.readString();
        name = in.readString();
        actualPrice = in.readString();
        priceAfterDiscount = in.readString();
        imageUrl = in.readString();
        seen = in.readString();
        business_type = in.readString();
        merchantName = in.readString();
        Description = in.readString();
        productType = in.readString();
    }

    public static final Creator<AdsIRbean> CREATOR = new Creator<AdsIRbean>() {
        @Override
        public AdsIRbean createFromParcel(Parcel in) {
            return new AdsIRbean(in);
        }

        @Override
        public AdsIRbean[] newArray(int size) {
            return new AdsIRbean[size];
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
    public void setDescription(String description) {
        this.Description = description;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
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
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
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
        parcel.writeString(Description);
        parcel.writeString(productType);
    }
}
