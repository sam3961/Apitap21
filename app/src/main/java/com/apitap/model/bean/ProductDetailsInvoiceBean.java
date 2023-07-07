package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 6/9/16.
 */

public class ProductDetailsInvoiceBean implements Parcelable {

    private String productImage;
    private String productId;
    public ProductDetailsInvoiceBean() {
    }


    private ProductDetailsInvoiceBean(Parcel in) {
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
    public static final Creator<ProductDetailsInvoiceBean> CREATOR = new Creator<ProductDetailsInvoiceBean>() {
        public ProductDetailsInvoiceBean createFromParcel(Parcel in) {
            return new ProductDetailsInvoiceBean(in);
        }

        public ProductDetailsInvoiceBean[] newArray(int size) {
            return new ProductDetailsInvoiceBean[size];

        }
    };

}
