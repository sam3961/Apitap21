package com.apitap.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shami on 9/5/2018.
 */

public class NewItemBean {

    private List<ParentBean> parentBeen;
    String title;

    public NewItemBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ParentBean> getParentBeen() {
        return parentBeen;
    }

    public void setParentBeen(List<ParentBean> childBeen) {
        this.parentBeen = childBeen;
    }

    public static class ChildBean {

        private String productType;
        private String imageUrl;
        private String productId;
        private String favorite;
        private String sellerName;
        private String seen;
        private String actualPrice;
        private String description;
        private String priceAfterDiscount;
        private String categoryName;
        private String Idhex;
        private List<ChildBean> meList;


        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getFavorite() {
            return favorite;
        }

        public void setFavorite(String favorite) {
            this.favorite = favorite;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getSeen() {
            return seen;
        }

        public void setSeen(String seen) {
            this.seen = seen;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIdhex() {
            return Idhex;
        }

        public void setIdhex(String idhex) {
            Idhex = idhex;
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

        public void setPriceAfterDiscount(String priceAfterDiscount) {
            this.priceAfterDiscount = priceAfterDiscount;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getTitle() {
            return Idhex;
        }

        public void setTitle(String hex) {
            this.Idhex = hex;
        }


    }

    public static class ParentBean {
        private String category;
        ArrayList<ChildBean> childBeen;


        public String getCategory() {
            return category;
        }

        public void setCategory(String name) {
            this.category = name;
        }

        public ArrayList<ChildBean> getChildBeen() {
            return childBeen;
        }

        public void setMeList(ArrayList<ChildBean> childBeen) {
            this.childBeen = childBeen;
        }
    }
}