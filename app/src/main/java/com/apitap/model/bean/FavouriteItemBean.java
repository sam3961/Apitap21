package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class FavouriteItemBean implements Parcelable {
    private String imageUrl;
    private String product_type;
    private String seen;
    private String Description;
    private String Id;
    private String actualPrice;
    private String discountedPrice;

    public FavouriteItemBean() {

    }

    public FavouriteItemBean(Parcel in) {
        imageUrl = in.readString();
        discountedPrice = in.readString();
        actualPrice = in.readString();
        product_type = in.readString();
        seen = in.readString();
        Description = in.readString();
        Id = in.readString();
    }

    public static final Creator<FavouriteItemBean> CREATOR = new Creator<FavouriteItemBean>() {
        @Override
        public FavouriteItemBean createFromParcel(Parcel in) {
            return new FavouriteItemBean(in);
        }

        @Override
        public FavouriteItemBean[] newArray(int size) {
            return new FavouriteItemBean[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public static Creator<FavouriteItemBean> getCREATOR() {
        return CREATOR;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setId(String id) {
        this.Id = id;
    }


    public String getSeen() {
        return seen;
    }


    public String getId() {
        return Id;
    }

    public String getproduct_type() {
        return product_type;
    }

    public void setproduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getDescription() {
        return Description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(discountedPrice);
        parcel.writeString(actualPrice);
        parcel.writeString(product_type);
        parcel.writeString(seen);
        parcel.writeString(Description);
        parcel.writeString(Id);

    }

}
