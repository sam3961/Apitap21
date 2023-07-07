package com.apitap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.bean.ProductDetailsInvoiceBean;
import com.apitap.model.bean.Sizedata;

import java.util.ArrayList;

/**
 * Created by sourcefuse on 6/9/16.
 */

public class DetailsInoviceBean implements Parcelable{

    private String image;
    private String merchantID;
    private String cartId;
    private String name;
    private String sellerName;
    private String productDesc;
    private String title;
    private ArrayList<Sizedata> sizedata;
    private ArrayList<ProductDetailsInvoiceBean> arrayProductDetails;
    public DetailsInoviceBean() {
    }


    private DetailsInoviceBean(Parcel in) {
        image = in.readString();
        name = in.readString();
        productDesc=in.readString();
        merchantID = in.readString();
        cartId = in.readString();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public ArrayList<ProductDetailsInvoiceBean> getArrayProductDetails() {
        return arrayProductDetails;
    }

    public void setArrayProductDetails(ArrayList<ProductDetailsInvoiceBean> arrayProductDetails) {
        this.arrayProductDetails = arrayProductDetails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Sizedata> getSizedata() {
        return sizedata;
    }

    public void setSizedata(ArrayList<Sizedata> sizedata) {
        this.sizedata = sizedata;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeString(productDesc);
        parcel.writeString(cartId);

    }
    public static final Creator<DetailsInoviceBean> CREATOR = new Creator<DetailsInoviceBean>() {
        public DetailsInoviceBean createFromParcel(Parcel in) {
            return new DetailsInoviceBean(in);
        }

        public DetailsInoviceBean[] newArray(int size) {
            return new DetailsInoviceBean[size];

        }
    };

}
