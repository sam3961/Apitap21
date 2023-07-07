package com.apitap.model.bean;

import java.io.Serializable;

/**
 * Created by sourcefuse on 13/9/16.
 */

public class ShoppingCompBean implements Serializable{

    private String shoppingCartId;
    private String shoppingCartDetailId;
    private String merchantId;
    private String itemCounter;
    private String totalAmount;
    private String companyName;
    private String companyImage;
    private String shoppingCartStatus;
    private String LastDate;
    private String Expiring;
    private String deliveryId;


    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public String getShoppingCartDetailId() {
        return shoppingCartDetailId;
    }

    public void setShoppingCartDetailId(String shoppingCartDetailId) {
        this.shoppingCartDetailId = shoppingCartDetailId;
    }
    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getItemCounter() {
        return itemCounter;
    }

    public void setItemCounter(String itemCounter) {
        this.itemCounter = itemCounter;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getShoppingCartStatus() {
        return shoppingCartStatus;
    }

    public void setShoppingCartStatus(String shoppingCartStatus) {
        this.shoppingCartStatus = shoppingCartStatus;
    }

    public String getExpiring() {
        return Expiring;
    }

    public void setExpiring(String expiring) {
        this.Expiring = expiring;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        this.LastDate = lastDate;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }
}
