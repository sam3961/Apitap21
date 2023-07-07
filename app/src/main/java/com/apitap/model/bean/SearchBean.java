package com.apitap.model.bean;

/**
 * Created by sourcefuse on 29/8/16.
 */

public class SearchBean {
    private String imageUrls;
    private String categoryName;
    private String productId;
    private String prodcutType;
    private String productName;
    private String isFavorite;
    private String sellerName;
    private String isSeen;
    private String actualPrice;
    private String priceAfterDiscount;
    private String description;

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProdcutType() {
        return prodcutType;
    }

    public void setProdcutType(String prodcutType) {
        this.prodcutType = prodcutType;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
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

    public String getProductName() {
        return productName;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
