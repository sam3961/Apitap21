package com.apitap.model.bean;

import java.util.List;

/**
 * Created by Shami on 9/5/2018.
 */

public class NewAdBean {

    String title;
    List<ChildBean> childBeen;

    public NewAdBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChildBean> getChildBeen() {
        return childBeen;
    }

    public void setChildBeen(List<ChildBean> childBeen) {
        this.childBeen = childBeen;
    }

    public static class ChildBean {
        private String id;
        private String AddId;
        private String merchantId;
        private String name;
        private String Merchantname;
        private String isSeen;
        private String imageUrl;
        private String videoUrl;
        private String desc;

        public ChildBean() {

        }

        public String getAddId() {
            return AddId;
        }

        public void setAddId(String addId) {
            AddId = addId;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMerchantname(String merchantname) {
            Merchantname = merchantname;
        }

        public void setIsSeen(String isSeen) {
            this.isSeen = isSeen;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public String getName() {
            return name;
        }

        public String getMerchantname() {
            return Merchantname;
        }

        public String getIsSeen() {
            return isSeen;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public String getDesc() {
            return desc;
        }
    }

}
