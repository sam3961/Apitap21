package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sourcefuse on 6/9/16.
 */

public class DetailsBean implements Parcelable{

    private String image;
    private String barcode;
    private String quantity;
    private String specs;
    private String sku;
    private String model;
    private String age21;
    private String age18;
    private String merchantID;
    private String name;
    private String brand;
    private String sellerName;
    private String productDesc;
    private String availability;
    private String title;
    private String price;
    private String special_price;
    private String price_AfterDiscount;
    private String limit;
    private ArrayList<Sizedata> sizedata;
    private ArrayList<ProductDetailsBean> arrayProductDetails;
    public DetailsBean() {
    }


    private DetailsBean(Parcel in) {
        image = in.readString();
        name = in.readString();
        productDesc=in.readString();
        merchantID = in.readString();
        price = in.readString();
        price_AfterDiscount = in.readString();
        special_price = in.readString();
        barcode = in.readString();
        specs = in.readString();
        sku = in.readString();
        model = in.readString();
        age21 = in.readString();
        age18 = in.readString();
        quantity = in.readString();
        brand = in.readString();
        availability = in.readString();
        limit = in.readString();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAge18() {
        return age18;
    }

    public void setAge18(String age18) {
        this.age18 = age18;
    }

    public String getAge21() {
        return age21;
    }

    public void setAge21(String age21) {
        this.age21 = age21;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(String special_price) {
        this.special_price = special_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPrice_AfterDiscount() {
        return price_AfterDiscount;
    }

    public void setPrice_AfterDiscount(String price_afterDiscount) {
        this.price_AfterDiscount = price_afterDiscount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public ArrayList<ProductDetailsBean> getArrayProductDetails() {
        return arrayProductDetails;
    }

    public void setArrayProductDetails(ArrayList<ProductDetailsBean> arrayProductDetails) {
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
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
        parcel.writeString(price);
        parcel.writeString(price_AfterDiscount);
        parcel.writeString(special_price);
        parcel.writeString(barcode);
        parcel.writeString(specs);
        parcel.writeString(sku);
        parcel.writeString(model);
        parcel.writeString(age21);
        parcel.writeString(age18);
        parcel.writeString(quantity);
        parcel.writeString(brand);
        parcel.writeString(availability);
        parcel.writeString(limit);


    }
    public static final Creator<DetailsBean> CREATOR = new Creator<DetailsBean>() {
        public DetailsBean createFromParcel(Parcel in) {
            return new DetailsBean(in);
        }

        public DetailsBean[] newArray(int size) {
            return new DetailsBean[size];

        }
    };

}
