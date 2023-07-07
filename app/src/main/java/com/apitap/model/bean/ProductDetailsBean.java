package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 6/9/16.
 */

public class ProductDetailsBean implements Parcelable {

    private String productImage;
    private String productId;
    public ProductDetailsBean() {
    }


    private ProductDetailsBean(Parcel in) {
        productImage = in.readString();
        productId = in.readString();
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productImage);
        parcel.writeString(productId);
    }
    public static final Creator<ProductDetailsBean> CREATOR = new Creator<ProductDetailsBean>() {
        public ProductDetailsBean createFromParcel(Parcel in) {
            return new ProductDetailsBean(in);
        }

        public ProductDetailsBean[] newArray(int size) {
            return new ProductDetailsBean[size];

        }
    };

}
