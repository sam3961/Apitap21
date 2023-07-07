package com.apitap.model.bean;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class Favspecialbean /*implements Parcelable*/{

    private String id;
    private String Idhex;
    private String name;
    private String productType;
    private String actualPrice;
    private String priceAfterDiscount;
    private String imageUrl;
    private String categoryName;
    private String business_type;

    public Favspecialbean(){

    }

 /*   protected Favdetailsbean(Parcel in) {
        id = in.readString();
        name = in.readString();
        actualPrice = in.readString();
        priceAfterDiscount = in.readString();
        imageUrl = in.readString();
        productType = in.readString();
        Idhex = in.readString();
        categoryName = in.readString();

    }

    public static final Creator<Favdetailsbean> CREATOR = new Creator<Favdetailsbean>() {
        @Override
        public Favdetailsbean createFromParcel(Parcel in) {
            return new Favdetailsbean(in);
        }

        @Override
        public Favdetailsbean[] newArray(int size) {
            return new Favdetailsbean[size];
        }
    };*/

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType= productType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHex() {
        return Idhex;
    }

    public void setIdHex(String hex) {
        this.Idhex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

  /*  @Override
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
        parcel.writeString(productType);
        parcel.writeString(Idhex);
        parcel.writeString(categoryName);
    }*/
}
