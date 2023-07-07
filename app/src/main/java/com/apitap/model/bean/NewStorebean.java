package com.apitap.model.bean;

import java.util.List;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class NewStorebean{

    private List<ChildBean> childBeen;

    public NewStorebean(){}

    public List<ChildBean> getChildBeen() {
        return childBeen;
    }

    public void setChildBeen(List<ChildBean> childBeen) {
        this.childBeen = childBeen;
    }

  public static class ChildBean {

      private String productType;
      private String actualPrice;
      private String priceAfterDiscount;
      private String categoryName;
      private String Idhex;
      private List<ME> meList;


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

      public List<ME> getMeList() {
          return meList;
      }

      public void setMeList(List<ME> childBeen) {
          this.meList = childBeen;
      }
  }

  public static class ME{
      private String id;
      private String imageUrl;
      private String name;

      public String getImageUrl() {
          return imageUrl;
      }

      public void setImageUrl(String imageUrl) {
          this.imageUrl = imageUrl;
      }
      public String getId() {
          return id;
      }

      public void setId(String id) {
          this.id = id;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }
  }
}
