package com.apitap.model.bean.items;

import java.util.HashMap;
import java.util.Map;

public class LocationBean {

    private String locationId;
    private String locationName;
    private String address;

    // 🔥 key = "choice1_choice2"
    private Map<String, Integer> choiceQuantityMap = new HashMap<>();

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // 🔹 Map access
    public Map<String, Integer> getChoiceQuantityMap() {
        return choiceQuantityMap;
    }

    // 🔹 Set quantity for (choice1 + choice2)
    public void setQuantityForChoices(String key, int qty) {
        choiceQuantityMap.put(key, qty);
    }

    // 🔹 Get quantity
    public int getQuantityForChoices(String key) {
        Integer q = choiceQuantityMap.get(key);
        return q == null ? 0 : q;
    }

    public boolean isInStockForChoices(String key) {
        return getQuantityForChoices(key) > 0;
    }
}
