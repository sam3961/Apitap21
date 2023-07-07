package com.apitap.model.bean;

/**
 * Created by sourcefuse on 7/9/16.
 */

public class SizeBean {

    private String size;
    private String id;
    private String type;

    public SizeBean(String size, String id, String type) {
        this.size = size;
        this.id = id;
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
