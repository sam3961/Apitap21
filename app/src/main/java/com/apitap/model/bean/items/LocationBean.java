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

        if (key == null || key.trim().isEmpty()) {
            Integer q = choiceQuantityMap.get("DEFAULT");
            return q == null ? 0 : q;
        }

        // 1️⃣ exact match
        Integer q = choiceQuantityMap.get(key);
        if (q != null) return q;

        if (key.contains("_")) {

            String[] parts = key.split("_");

            // 2️⃣ try second choice (this is your real inventory key)
            q = choiceQuantityMap.get(parts[1]);
            if (q != null) return q;

            // 3️⃣ try first choice
            q = choiceQuantityMap.get(parts[0]);
            if (q != null) return q;
        }

        // 4️⃣ default fallback
        Integer defaultQty = choiceQuantityMap.get("DEFAULT");
        return defaultQty == null ? 0 : defaultQty;
    }

    public boolean isInStockForChoices(String key) {
        return getQuantityForChoices(key) > 0;
    }
}
