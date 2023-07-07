package com.apitap.model.bean;

public class ReturnBean {

    public String stringQuantity;
    public String imageUrl;
    public String amount;
    public String itemName;
    public String choiceOne;
    public String choiceTwo;
    public String productId;
    public String comments="";
    public String reasonId="";


    public ReturnBean(){
    }
    public  ReturnBean(String stringQuantity,String imageUrl,String amount,String itemName,String choiceOne,String choiceTwo,String productId){
        this.amount=amount;
        this.stringQuantity=stringQuantity;
        this.choiceOne=choiceOne;
        this.choiceTwo =choiceTwo;
        this.itemName=itemName;
        this.imageUrl=imageUrl;
        this.productId=productId;
    }

    public String getStringQuantity() {
        return stringQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAmount() {
        return amount;
    }

    public String getItemName() {
        return itemName;
    }

    public String getChoiceOne() {
        return choiceOne;
    }

    public String getChoiceTwo() {
        return choiceTwo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setStringQuantity(String stringQuantity) {
        this.stringQuantity = stringQuantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setChoiceOne(String choiceOne) {
        this.choiceOne = choiceOne;
    }

    public void setChoiceTwo(String choiceTwo) {
        this.choiceTwo = choiceTwo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }
}
