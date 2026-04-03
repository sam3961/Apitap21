package com.apitap.model.reservation;

public class StoreItem {
    private String id;
    private String merchantId;
    private String name;

    public StoreItem(String id, String name,String merchantId) {
        this.id = id;
        this.name = name;
        this.merchantId = merchantId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMerchantId() {
        return merchantId;
    }

    @Override
    public String toString() {
        return name;
    }
}